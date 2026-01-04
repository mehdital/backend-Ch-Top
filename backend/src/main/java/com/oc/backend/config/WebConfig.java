package com.oc.backend.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * Configuration Web MVC pour servir les fichiers uploadés depuis le disque.
 *
 * <p>Exposition :
 * <ul>
 *   <li>URL publique : {@code /uploads/**}</li>
 *   <li>Dossier local : {@code app.upload-dir} (voir {@code application.yaml})</li>
 * </ul>
 */
public class WebConfig implements WebMvcConfigurer {
  private final Path uploadDir;

  public WebConfig(@Value("${app.upload-dir}") String uploadDir) {
    this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:" + uploadDir + "/");
  }
}
