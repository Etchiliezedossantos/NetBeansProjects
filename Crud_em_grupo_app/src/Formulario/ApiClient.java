/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formulario;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *
 * @author johny
 */
public class ApiClient {
    private static final String BASE_URL = "http://localhost:8000/api";
    
    public static String extrairIdDaResposta(String resposta) {
    try {
        // Procura o padrão "id":número no JSON
        int startIndex = resposta.indexOf("\"id\":") + 5;
        int endIndex = resposta.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = resposta.indexOf("}", startIndex);
        
        return resposta.substring(startIndex, endIndex).trim();
    } catch (Exception e) {
        return "1"; // fallback se der erro
    }
}
    
    public static String fazerRequisicao(String endpoint, String metodo, String jsonBody){
        try{
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod(metodo);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            
            //se tiver corpo (post) enviar
            if(jsonBody != null && !jsonBody.isEmpty()){
                try(OutputStream os = connection.getOutputStream()){
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            
            //ler resposta
            int status = connection.getResponseCode();
            InputStream inputStream = (status < 400) ? connection.getInputStream() : connection.getErrorStream();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            reader.close();
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"Erro de conexão: \" + e.getMessage()}";
        }
    }
}
