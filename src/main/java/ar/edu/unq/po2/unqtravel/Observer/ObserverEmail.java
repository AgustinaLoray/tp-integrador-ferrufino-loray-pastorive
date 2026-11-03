package ar.edu.unq.po2.unqtravel.Observer;

public class ObserverEmail {
    
    private MailSender mailSender;
    private String emailCliente;

    public ObserverEmail(MailSender mailSender, String emailCliente) {
        this.mailSender = mailSender;
        this.emailCliente = emailCliente;
    }

    public void notificar(){
        this.mailSender.enviarMail(
            this.emailCliente, 
            "Actualizacion de reserva", 
            "Su reserva cambio de estado",
            null);
    }
}
