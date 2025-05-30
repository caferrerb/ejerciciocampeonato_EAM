// Archivo: util/JsonDataManager.java
package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Campeonato; // Importar la clase Campeonato
import java.io.File;
import java.io.IOException;

/**
 * Gestiona la persistencia de los datos del Campeonato en formato JSON utilizando la librería Jackson.
 */
public class JsonDataManager {
    private final ObjectMapper objectMapper;
    private final String filePath;

    /**
     * Constructor para JsonDataManager.
     *
     * @param filePath La ruta del archivo donde se guardarán/cargarán los datos JSON.
     */
    public JsonDataManager(String filePath) {
        this.objectMapper = new ObjectMapper();
        // Registrar el módulo para soportar tipos de Java 8 Date/Time API (como LocalDateTime)
        // Habilitar el "pretty printing" para que el JSON sea legible
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Deshabilitar la escritura de fechas como timestamps, para usar formato ISO 8601

        this.filePath = filePath;
    }

    /**
     * Guarda el objeto Campeonato actual en un archivo JSON.
     *
     * @param campeonato El objeto Campeonato a guardar.
     */
    public void save(Campeonato campeonato) {
        try {
            objectMapper.writeValue(new File(filePath), campeonato);
            System.out.println("Datos del campeonato guardados en: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos del campeonato: " + e.getMessage());
        }
    }

    /**
     * Carga los datos del Campeonato desde un archivo JSON.
     *
     * @return El objeto Campeonato cargado, o una nueva instancia de Campeonato si el archivo no existe o hay un error.
     */
    public Campeonato load() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Campeonato loadedCampeonato = objectMapper.readValue(file, Campeonato.class);
                System.out.println("Datos del campeonato cargados desde: " + filePath);
                // Jackson suele manejar bien las referencias bidireccionales y los objetos anidados
                // si los constructores y setters son adecuados.
                // Sin embargo, para relaciones complejas como las listas de Jugadores en Equipo
                // y Partidos en Equipo, es crucial que Jackson pueda reconstruir el grafo de objetos.
                // Esto generalmente funciona bien si no hay ciclos de referencia complejos que Jackson no pueda resolver.
                // Si se encuentran problemas de deserialización (ej. NullPointerException en objetos anidados),
                // podría ser necesario un post-procesamiento para reestablecer referencias manualmente.
                return loadedCampeonato;
            } catch (IOException e) {
                System.err.println("Error al cargar los datos del campeonato: " + e.getMessage() + ". Se creará un nuevo campeonato.");
            }
        } else {
            System.out.println("Archivo de datos no encontrado. Se creará un nuevo campeonato.");
        }
        return new Campeonato(); // Retorna un nuevo campeonato si no se puede cargar o el archivo no existe
    }
}
