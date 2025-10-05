package com.fitzealot.fitnessService.util;

import com.fitzealot.fitnessService.model.dto.WorkoutPlanDto;
import com.fitzealot.fitnessService.model.dto.DailyWorkoutDto;
import com.fitzealot.fitnessService.model.dto.ExerciseDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to, WorkoutPlanDto plan) throws MessagingException {

        String subject = "Your Personalized Workout Plan - FitZealot";
        String htmlBody = buildHtmlBody(plan);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true indicates HTML
        helper.setFrom("chathurabasnayake2007@gmail.com");

        mailSender.send(message);
        System.out.println("✅ Email sent to: " + to);
    }

    private String buildHtmlBody(WorkoutPlanDto plan) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<style>");
        html.append("body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; }");
        html.append(".container { max-width: 650px; margin: 0 auto; background-color: #ffffff; }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px 30px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 28px; font-weight: 700; letter-spacing: 1px; }");
        html.append(".header .emoji { font-size: 36px; display: block; margin-bottom: 10px; }");
        html.append(".content { padding: 30px; }");
        html.append(".greeting { font-size: 18px; color: #333; margin-bottom: 20px; }");
        html.append(".summary-box { background-color: #f8f9ff; border-left: 4px solid #667eea; padding: 20px; margin: 25px 0; border-radius: 5px; }");
        html.append(".summary-box h2 { margin: 0 0 15px 0; color: #667eea; font-size: 18px; }");
        html.append(".summary-item { margin: 8px 0; color: #555; font-size: 15px; }");
        html.append(".summary-item strong { color: #333; }");
        html.append(".day-card { background-color: #ffffff; border: 2px solid #e0e0e0; border-radius: 10px; padding: 20px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        html.append(".day-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px 18px; border-radius: 8px; margin: -20px -20px 15px -20px; }");
        html.append(".day-title { font-size: 20px; font-weight: 700; margin: 0; }");
        html.append(".day-subtitle { font-size: 14px; margin: 5px 0 0 0; opacity: 0.9; }");
        html.append(".rest-day { background-color: #fff3e0; border-left: 4px solid #ff9800; padding: 15px; border-radius: 5px; text-align: center; font-size: 16px; color: #e65100; }");
        html.append(".section-title { color: #667eea; font-size: 16px; font-weight: 600; margin: 15px 0 10px 0; display: flex; align-items: center; }");
        html.append(".section-title .icon { margin-right: 8px; font-size: 18px; }");
        html.append(".exercise-list { list-style: none; padding: 0; margin: 10px 0; }");
        html.append(".exercise-item { background-color: #f8f9ff; padding: 12px 15px; margin: 8px 0; border-radius: 6px; border-left: 3px solid #667eea; }");
        html.append(".exercise-name { font-weight: 600; color: #333; font-size: 15px; margin-bottom: 5px; }");
        html.append(".exercise-details { color: #666; font-size: 14px; }");
        html.append(".warmup-item, .cooldown-item { padding: 8px 0; color: #555; font-size: 14px; }");
        html.append(".warmup-item:before { content: '•'; color: #ff6b6b; font-weight: bold; margin-right: 8px; }");
        html.append(".cooldown-item:before { content: '•'; color: #51cf66; font-weight: bold; margin-right: 8px; }");
        html.append(".tips-box { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 25px; margin: 30px 0; border-radius: 10px; }");
        html.append(".tips-box h2 { margin: 0 0 15px 0; font-size: 20px; }");
        html.append(".tip-item { padding: 8px 0; font-size: 15px; }");
        html.append(".tip-item:before { content: '✓'; margin-right: 10px; font-weight: bold; }");
        html.append(".footer { background-color: #f8f9ff; padding: 25px; text-align: center; color: #666; }");
        html.append(".footer .motivation { font-size: 18px; font-weight: 600; color: #667eea; margin-bottom: 15px; }");
        html.append(".footer .signature { font-size: 14px; line-height: 1.6; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<span class='emoji'>🏋️</span>");
        html.append("<h1>FITZEALOT WORKOUT PLAN</h1>");
        html.append("</div>");

        // Content
        html.append("<div class='content'>");
        html.append("<div class='greeting'>Hello <strong>").append(plan.getUsername()).append("</strong>! 👋</div>");
        html.append("<p style='color: #555; font-size: 16px;'>Your personalized workout plan is ready! Let's get started on your fitness journey.</p>");

        // Summary Box
        html.append("<div class='summary-box'>");
        html.append("<h2>📋 PLAN SUMMARY</h2>");
        html.append("<div class='summary-item'><strong>Duration:</strong> ").append(plan.getDurationWeeks()).append(" weeks</div>");
        html.append("<div class='summary-item'><strong>Created:</strong> ").append(plan.getCreatedAt()).append("</div>");
        html.append("<div class='summary-item'><strong>Total Days:</strong> ").append(plan.getWeeklyPlan().size()).append("</div>");
        html.append("</div>");

        // Weekly Schedule Title
        html.append("<h2 style='color: #333; margin-top: 30px; text-align: center;'>📅 YOUR WEEKLY SCHEDULE</h2>");

        // Daily Workouts
        int dayCount = 1;
        for (DailyWorkoutDto day : plan.getWeeklyPlan()) {
            html.append("<div class='day-card'>");
            html.append("<div class='day-header'>");
            html.append("<div class='day-title'>DAY ").append(dayCount++).append(": ").append(day.getDayOfWeek().toUpperCase()).append("</div>");
            html.append("<div class='day-subtitle'>").append(day.getWorkoutType()).append("</div>");
            html.append("</div>");

            if (day.isRestDay()) {
                html.append("<div class='rest-day'>🛌 REST DAY - Recovery is important!</div>");
            } else {
                // Warm-up
                if (day.getWarmUp() != null && !day.getWarmUp().isEmpty()) {
                    html.append("<div class='section-title'><span class='icon'>🔥</span> Warm-up</div>");
                    for (String warmup : day.getWarmUp()) {
                        html.append("<div class='warmup-item'>").append(warmup).append("</div>");
                    }
                }

                // Exercises
                if (day.getExercises() != null && !day.getExercises().isEmpty()) {
                    html.append("<div class='section-title'><span class='icon'>💪</span> Exercises</div>");
                    html.append("<ul class='exercise-list'>");
                    for (ExerciseDetailDto exercise : day.getExercises()) {
                        html.append("<li class='exercise-item'>");
                        html.append("<div class='exercise-name'>").append(exercise.getName()).append("</div>");
                        html.append("<div class='exercise-details'>Sets: ").append(exercise.getSets())
                                .append(" | Reps: ").append(exercise.getReps()).append("</div>");
                        html.append("</li>");
                    }
                    html.append("</ul>");
                }

                // Cool-down
                if (day.getCoolDown() != null && !day.getCoolDown().isEmpty()) {
                    html.append("<div class='section-title'><span class='icon'>🧘</span> Cool-down</div>");
                    for (String cooldown : day.getCoolDown()) {
                        html.append("<div class='cooldown-item'>").append(cooldown).append("</div>");
                    }
                }
            }

            html.append("</div>");
        }

        // Tips Box
        html.append("<div class='tips-box'>");
        html.append("<h2>💡 TIPS FOR SUCCESS</h2>");
        html.append("<div class='tip-item'>Stay hydrated throughout your workouts</div>");
        html.append("<div class='tip-item'>Listen to your body and rest when needed</div>");
        html.append("<div class='tip-item'>Maintain proper form over heavy weights</div>");
        html.append("<div class='tip-item'>Track your progress weekly</div>");
        html.append("</div>");

        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<div class='motivation'>Keep pushing! You've got this! 💪</div>");
        html.append("<div class='signature'>Best regards,<br><strong>The FitZealot Team</strong></div>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}