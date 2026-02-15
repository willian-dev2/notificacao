package com.vinicius.notificacao.business;

import com.vinicius.notificacao.business.dto.TarefasDTO;
import com.vinicius.notificacao.infrastructure.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    // Injeções de dependência
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    // Anotação Value e expressão que indica a chave da propriedade que será buscada no application.yaml
    @Value("${envio.email.remetente}")
    public String remetente;

    @Value("${envio.email.nomeRemetente}")
    private String nomeRemetente;


    public void enviaEmail(TarefasDTO dto) {

        try {
            //Cria um email que suporta padrão MIME (texto, html...)
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            //Simplifica a configuração da mensagem/ True = email pode ter multipart (anexos...)
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            //Aqui define o remetente, destinatário, e assunto do email.
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            mimeMessageHelper.setTo(InternetAddress.parse(dto.getEmailUsuario()));
            mimeMessageHelper.setSubject("notificação de tarefa");

            //Cria um contexto que será substituída no template(thymeleaf)
            Context context = new Context();
            //Ocorre o processo de arquivo template("notificacao") e substitui pelos get.dto nas areas requisitadas
            context.setVariable("nomeTarefa", dto.getNomeTarefa());
            context.setVariable("dataEvento", dto.getDataEvento());
            context.setVariable("descricao", dto.getDescricao());
            String template = templateEngine.process("notificacao", context);
            //Define o corpo do email como HTML (true está indicando que é html)
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mensagem);


        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar o email ", e.getCause());
        }
    }

}
