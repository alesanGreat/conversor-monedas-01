import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Scanner; // Importar la clase Scanner para interactuar con el usuario

public class ConversorDeMonedas {
    private static final String API_KEY = "a54e17f27ac9bd0bf0809fde";

    public double convertirMoneda(double cantidad, String monedaOriginal, String monedaDestino) {
        try {
            // Crear cliente HTTP
            HttpClient cliente = HttpClient.newHttpClient();

            // Construir la solicitud HTTP
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + monedaOriginal))
                    .GET()
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

            // Analizar la respuesta en formato JSON
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(respuesta.body(), JsonObject.class);

            if (json.get("conversion_rates") != null) {
                JsonObject rates = json.getAsJsonObject("conversion_rates");
                double tasaCambio = rates.get(monedaDestino).getAsDouble();

                // Convertir la moneda
                return cantidad * tasaCambio;
            } else {
                System.out.println("No se encontraron tasas de cambio para la moneda especificada.");
                return 0;
            }
        } catch (Exception e) {
            System.out.println("Error al convertir la moneda: " + e.getMessage());
            return 0;
        }
    }

    public static void main(String[] args) {
        ConversorDeMonedas conversor = new ConversorDeMonedas();
        Scanner scanner = new Scanner(System.in);

        try {
            // Interactuar con el usuario
            System.out.println("Ingrese la cantidad de moneda a convertir:");
            double cantidad = scanner.nextDouble();
            scanner.nextLine(); // Consumir la línea restante después de leer el número

            System.out.println("Ingrese la moneda original (ejemplo: USD):");
            String monedaOriginal = scanner.nextLine().toUpperCase();

            System.out.println("Ingrese la moneda destino (ejemplo: EUR):");
            String monedaDestino = scanner.nextLine().toUpperCase();

            // Convertir la moneda
            double resultado = conversor.convertirMoneda(cantidad, monedaOriginal, monedaDestino);

            // Mostrar el resultado
            System.out.println("El resultado de la conversión es: " + resultado);
        } finally {
            scanner.close(); // Asegurar que el Scanner se cierra adecuadamente
        }
    }
}
