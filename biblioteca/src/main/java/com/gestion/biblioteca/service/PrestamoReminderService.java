package com.gestion.biblioteca.service;

import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.repository.PrestamoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PrestamoReminderService {


    private final PrestamoRepository prestamoRepository;
    private final EmailService emailService;


    @Scheduled(cron = "0 0 1 * * *")
    public void sendUpcomingDueDateReminders() {
        log.info("Iniciando tarea programada: Verificando préstamos próximos a vencer...");


        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);

        List<Prestamo> prestamosPorVencer = prestamoRepository.findByFechaLimiteAndDevueltoFalse(twoDaysFromNow);

        log.info("Se encontraron {} préstamos que vencen en 2 días. Enviando recordatorios...", prestamosPorVencer.size());

        for (Prestamo prestamo : prestamosPorVencer) {
            String userEmail = prestamo.getUsuario().getMail();
            String userName = prestamo.getUsuario().getNombreCompleto();
            String bookTitle = prestamo.getLibro().getTitulo();
            LocalDate dueDate = prestamo.getFechaLimite();

            String subject = "Recordatorio Importante: Tu libro '" + bookTitle + "' vence pronto";
            String body = "Hola " + userName + ",\n\n" +
                    "Esperamos que estés disfrutando de tu lectura.\n\n" +
                    "Queremos recordarte que tu préstamo del libro **'" + bookTitle + "'** vence en **2 días** (fecha límite: " + dueDate + ").\n" +
                    "Por favor, asegúrate de devolverlo a tiempo para evitar cualquier multa.\n\n" +
                    "¡Gracias por usar nuestros servicios!\n\n" +
                    "Atentamente,\nLa Biblioteca";

            emailService.sendEmail(userEmail, subject, body);
        }
        log.info("Tarea programada de recordatorios finalizada.");
    }
}

