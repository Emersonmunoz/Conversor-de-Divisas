import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class CurrencyConverterApp {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/5d63e511e12a8b5cdd37e17c/latest/USD";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("¡Bienvenido(a) a la aplicación de conversor de monedas del reto Alura G6!");

        boolean running = true;
        while (running) {
            displayMenu();
            int option = getUserOption();

            switch (option) {
                case 1:
                    convertCurrency("USD", "ARS", "Peso Argentino");
                    break;
                case 2:
                    convertCurrency("ARS", "USD", "Dólar");
                    break;
                case 3:
                    convertCurrency("USD", "BRL", "Real Brasileño");
                    break;
                case 4:
                    convertCurrency("BRL", "USD", "Dólar");
                    break;
                case 5:
                    convertCurrency("USD", "COP", "Peso Colombiano");
                    break;
                case 6:
                    convertCurrency("COP", "USD", "Dólar");
                    break;
                case 7:
                    System.out.println("¡Gracias por usar nuestra aplicación de conversión de monedas!");
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor selecciona una opción del 1 al 7.");
                    break;
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n*** Menú de Conversión ***");
        System.out.println("1. Dólar a Peso Argentino");
        System.out.println("2. Peso Argentino a Dólar");
        System.out.println("3. Dólar a Real Brasileño");
        System.out.println("4. Real Brasileño a Dólar");
        System.out.println("5. Dólar a Peso Colombiano");
        System.out.println("6. Peso Colombiano a Dólar");
        System.out.println("7. Salir");
    }

    private static int getUserOption() {
        System.out.print("Selecciona una opción: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor ingresa un número válido: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void convertCurrency(String sourceCurrencyCode, String targetCurrencyCode, String targetCurrencyName) {
        double amountToConvert = getAmountToConvert();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                double exchangeRate = jsonObject.getAsJsonObject("conversion_rates").get(targetCurrencyCode).getAsDouble();
                double convertedAmount = amountToConvert * exchangeRate;

                System.out.printf("El valor de %.2f %s corresponde a %.2f %s\n", amountToConvert, sourceCurrencyCode, convertedAmount, targetCurrencyName);
            } else {
                System.out.println("Error al obtener las tasas de cambio. Código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al realizar la solicitud HTTP: " + e.getMessage());
        }
    }

    private static double getAmountToConvert() {
        System.out.print("Ingresa la cantidad de divisa a convertir: ");
        while (!scanner.hasNextDouble()) {
            System.out.print("Por favor ingresa un número válido: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
