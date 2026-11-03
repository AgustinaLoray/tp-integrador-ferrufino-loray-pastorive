package ar.edu.unq.po2.unqtravel.Observer;

public interface MailSender {  //interfaz para los envios de mail
    void enviarMail(String direccionDestino, String titulo, String mensaje, Object adjunto);
}
