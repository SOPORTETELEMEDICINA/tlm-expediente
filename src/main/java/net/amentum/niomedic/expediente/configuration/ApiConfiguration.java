package net.amentum.niomedic.expediente.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
@Slf4j
public class ApiConfiguration {
   @Value("${url}")
   private String urlProperti;
   private ObjectMapper mapp = new ObjectMapper();
   private Date fechaExpiraTocken;
   String token = "";

   public Map<String, Object> obtenerToken() throws ConsultaException {
      try {
         log.info("obtenerToken() - Solicitando token de acceso a: {} ", urlProperti);
         String params = "client_id=auth.testing&client_secret=auth.testing&username=sysAdmin&password=5ae23bbbb73b35ef9f4a624e656b8240641dc48e005b55482def92901253389f&grant_type=password";
         URL url = new URL(urlProperti + "auth/oauth/token");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setDoOutput(true);
         conn.setRequestMethod("POST");
         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         OutputStream os = conn.getOutputStream();
         os.write(params.getBytes());
         os.flush();
         if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            System.out.println(conn.getResponseMessage() + "  " + conn.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
               response.append(currentLine);
            }
            br.close();
            response.toString();
            ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
            consE.addError("No fue posible obtener detalles Consulta");
            log.info("obtenerToken() - Ocurrio un error al obtenero el token - error: {}", response.toString());
            throw consE;
         } else {
            log.info("obtenerToken() - El token se generó exitosamente");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
               response.append(currentLine);
            }
            br.close();
            Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
            Map<String, Object> infoToken = new HashMap<String, Object>();
            Integer expiraEn = (Integer) JsonResponse.get("expires_in");
            infoToken.put("expires_in", expiraEn);
            infoToken.put("access_token", (String) JsonResponse.get("access_token"));
            infoToken.put("hier_token", (String) JsonResponse.get("hier_token"));
            final Long one_second_in_millis = 1000L;
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAdding = new Date(t + (expiraEn * one_second_in_millis));
            fechaExpiraTocken = afterAdding;
            return infoToken;
         }
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible crear el token para completar la información de los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener el token para los reportes - error: {}", e);
         throw consE;
      }
   }

   public Boolean tockenActivo() {
      Date fechaActual = new Date();
      if (fechaActual.getTime() > fechaExpiraTocken.getTime()) {
         return false;
      } else {
         return true;
      }
   }

   public Map<String, Object> getUserAppById(int idUserApp) throws ConsultaException {
      try {
         log.info("getUserAppById() - Solicitando detalles del usuario a: {} ", urlProperti);
         URL url = new URL(urlProperti + "users/" + idUserApp);
         int contador = 0;
         boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null)
                  response.append(currentLine);
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de la consulta para usuario", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles del usuario");
                  log.info("getUserAppById() - Ocurrio un error al obtener el detalle del usuario- error: {}", response);
                  throw consE;
               }
            } else {
               log.info("getUserAppById() - Se obtuvieron los detalles del usuario exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               return mapp.readValue(response.toString(), Map.class);
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion del usuario");
         log.error("Ocurrio un error inesperado al obtener los detalles del usuario- error: {}", e.getCause().toString());
         throw consE;
      }
   }

   //obtener informacion del paciente
   public Map<String, Object> getPacieteByid(String idPaciente) throws ConsultaException {
      try {
         log.info("getPacieteByid() - Solicitando detalles del paciente a: {} ", urlProperti);
         URL url = new URL(urlProperti + "pacientes/" + idPaciente);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
//				if(token.isEmpty()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}else if(!tockenActivo()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles del paciente");
                  log.info("getPacieteByid() - Ocurrio un error en los reportes al obtener los detalles del paciente - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getPacieteByid() - Se obtuvieron los detalles del paciente exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion del paciente para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener los detalles del paciente para los reportes - error: {}", e);
         throw consE;
      }
   }

   //obtener informacion del medico
   public Map<String, Object> getMedicoByid(String idMedico) throws ConsultaException {
      try {
         log.info("getMedicoByid() - Solicitando detalles del Medico a: {} ", urlProperti);
         URL url = new URL(urlProperti + "medicos/" + idMedico);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");

//				if(token.isEmpty()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}else if(!tockenActivo()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles del medico");
                  log.info("getMedicoByid() - Ocurrio un error en los reportes al obtener los detalles del medico - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getMedicoByid() - Se obtuvieron los detalles del medico exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion del medico para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener los detalles del medico para los reportes - error: {}", e);
         throw consE;
      }
   }


   //obtener informacion del medico
   public Map<String, Object> getRecetaByidConsulta(Long idConsulta) throws ConsultaException {
      try {
         log.info("getRecetaByidConsulta() - Solicitando detalles de la receta a: {} ", urlProperti);
         URL url = new URL(urlProperti + "receta/por-consulta/" + idConsulta);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
//				if(token.isEmpty()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}else if(!tockenActivo()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles de la receta");
                  log.info("getRecetaByidConsulta() - Ocurrio un error en los reportes al obtener los detalles la receta - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getRecetaByidConsulta() - Se obtuvieron los detalles de la receta exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de la receta para los formatos impreso");
         log.error("getRecetaByidConsulta () - Ocurrio un error inesperado al obtener los detalles de la receta para los reportes - error: {}", e);
         throw consE;
      }
   }

   //obtener informacion del UnidadMedica
   public Map<String, Object> getUnidadMeicaByid(Long idUnidadMedica) throws ConsultaException {
      try {
         log.info("getUnidadMeicaByid() - Solicitando detalles de la Unidad Medica a: {} ", urlProperti);
         URL url = new URL(urlProperti + "unidad-medica/" + idUnidadMedica);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
//				if(token.isEmpty()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}else if(!tockenActivo()) {
//					Map<String,Object> infoTocken =obtenerToken();
//					token ="bearer "+(String) infoTocken.get("access_token");
//				}
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles de la Unidad medica");
                  log.info("getUnidadMeicaByid() - Ocurrio un error en los reportes al obtener los detalles la Unidad Medica - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getUnidadMeicaByid() - Se obtuvieron los detalles de la Unidad Medica exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de la receta para los formatos impreso");
         log.error("getUnidadMeicaByid () - Ocurrio un error inesperado al obtener los detalles de la Unidad Medica para los reportes - error: {}", e);
         throw consE;
      }
   }


   //obtener informacion del medico
   public List<Map<String, Object>> getEstudioByIdConsulta(Long idConsulta) throws ConsultaException {
      try {
         log.info("getEstudioByIdConsulta() - Solicitando detalles del Estudio a: {} ", urlProperti);
         URL url = new URL(urlProperti + "estudio/por-consulta/" + idConsulta);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
//					if(token.isEmpty()) {
//						Map<String,Object> infoTocken =obtenerToken();
//						token ="bearer "+(String) infoTocken.get("access_token");
//					}else if(!tockenActivo()) {
//						Map<String,Object> infoTocken =obtenerToken();
//						token ="bearer "+(String) infoTocken.get("access_token");
//					}
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError(response.toString());
                  log.info("getEstudioByIdConsulta() - Ocurrio un error en los reportes al obtener los detalles del estudio - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getEstudioByIdConsulta() - Se obtuvieron los detalles del estudio");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               List<Map<String, Object>> JsonResponse = mapp.readValue(response.toString(), List.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de los estudios para los formatos impreso");
         log.error("getEstudioByIdConsulta() - Ocurrio un error inesperado al obtener los detalles de los estudios para los reportes - error: {}", e);
         throw consE;
      }
   }

   // obtener estudios por folio
   public Map<String, Object> getEstudioByFolio(Long folio) throws ConsultaException {
      try {
         log.info("getEstudioByFolio() - Solicitando detalles del Estudio a: {} ", urlProperti);
         URL url = new URL(urlProperti + "estudio/por-folio/" + folio);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
//					if(token.isEmpty()) {
//						Map<String,Object> infoTocken =obtenerToken();
//						token ="bearer "+(String) infoTocken.get("access_token");
//					}else if(!tockenActivo()) {
//						Map<String,Object> infoTocken =obtenerToken();
//						token ="bearer "+(String) infoTocken.get("access_token");
//					}
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError(response.toString());
                  log.info("getEstudioByFolio() - Ocurrio un error en los reportes al obtener los detalles del estudio - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getEstudioByFolio() - Se obtuvieron los detalles del estudio");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
//               List<Map<String, Object>> JsonResponse = mapp.readValue(response.toString(), List.class);
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de los estudios para los formatos impreso");
         log.error("getEstudioByFolio() - Ocurrio un error inesperado al obtener los detalles de los estudios para los reportes - error: {}", e);
         throw consE;
      }
   }


   // Inicia GGR20200619 Método para pedir un grupo por ID a nio-security
   public Map<String, Object> getGrupoById(Long idGroup) throws ConsultaException {
      try {
         log.info("getGrupoByid() - Solicitando detalles del Grupo a: {} ", urlProperti);
         URL url = new URL(urlProperti + "groups/" + idGroup);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles del medico");
                  log.info("getMedicoByid() - Ocurrio un error en los reportes al obtener los detalles del grupo - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getMedicoByid() - Se obtuvieron los detalles del grupo exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               String laImagenresponse = (String) JsonResponse.get("imagen");
               int index = laImagenresponse.indexOf("base64,");
               laImagenresponse = laImagenresponse.substring(index + 7, laImagenresponse.length());
               JsonResponse.put("imagen", laImagenresponse);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion del Grupo en Sesión para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener los detalles del Grupo en Sesión para los reportes - error: {}", e);
         throw consE;
      }
      // Fin GGR20200619
   }

   // GGR20200709 Agrego método para buscar horario por idUser
   public Map<String, Object> getHorarioByIdUser(Long idUser) throws ConsultaException {
      try {
         log.info("getHorarioByIdUser() - Solicitando horario de Usuario: {} ", urlProperti);
         URL url = new URL(urlProperti + "users/horario/" + idUser);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener  horario de Usuario");
                  log.info("getHorarioByIdUser() - Ocurrio un error en los reportes al obtener horario de Usuario - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getHorarioByIdUser() - Se obtuvo horario de Usuario exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion horario de Usuario para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener horario de Usuario para los reportes - error: {}", e);
         throw consE;
      }
   }
   //Fin GGR20200709


   public Map<String, Object> getHistoriaClinicaByIdPaciente(String idPaciente) throws ConsultaException {
      log.info("getHistoriaClinicaByIdPaciente() - Solicitando url: {} ", urlProperti);
      log.info("getHistoriaClinicaByIdPaciente() - Paciente: {} ", idPaciente);
      try {
         log.info("getHorarioByIdUser() - Solicitando historia clinica de Usuario: {} ", urlProperti);
         URL url = new URL(urlProperti + "historia-clinica/" + idPaciente);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener la historia clinica de Usuario");
                  log.info("getHorarioByIdUser() - Ocurrio un error en los reportes al obtener la historia clinica - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getHistoriaClinicaByIdPaciente() - Se obtuvo la historia clinica de Usuario exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de la historia clinica de Usuario para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener la historia clinica de Usuario para los reportes - error: {}", e);
         throw consE;
      }
   }

   public Map<String, Object> getCursoClinico(String idPaciente) throws ConsultaException {
      try {
         URL url = new URL(urlProperti + "cursoclinico/search?idPaciente=" + idPaciente);
         log.info("getCursoClinico() - Paciente: {} ", idPaciente);
         log.info("getCursoClinico() - Solicitando curso clinico de Usuario: {} ", url.toString());
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener el curso clinico de Usuario");
                  log.info("getCursoClinico() - Ocurrio un error en los reportes al obtener el curso clinico - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getCursoClinico() - Se obtuvo la historia clinica de Usuario exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion del curso clinico de Usuario para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener el curso clinico de Usuario para los reportes - error: {}", e);
         throw consE;
      }
   }

   //https://bajacalifornia.telemedicina.lat:9211/cursoclinico/lista-consultas?idPaciente=881de6d0-349e-4a01-af80-1a93ab1b1a94&idPadecimiento=111
   public Map<String, Object> getListaConsulta(String idPaciente, String idPadecimiento) throws ConsultaException {
      try {
         URL url = new URL(urlProperti + "cursoclinico/lista-consultas?idPaciente=" + idPaciente + "&idPadecimiento=" + idPadecimiento);
         log.info("getListaConsulta() - Paciente: {} - Padecimiento: {} ", idPaciente,idPadecimiento);
         log.info("getListaConsulta() - Solicitando lista consulta de Usuario: {} ", url.toString());
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener lista consulta de Usuario");
                  log.info("getListaConsulta() - Ocurrio un error en los reportes al obtener lista consulta - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getListaConsulta() - Se obtuvo la lista consulta de Usuario exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de lista consulta de Usuario para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener lista consulta de Usuario para los reportes - error: {}", e);
         throw consE;
      }
   }

   public Map<String, Object> getUnidadMedicaByid(Integer idUnidadMedica) throws ConsultaException {
      try {
         log.info("getUnidadMedicaByid() - Solicitando detalles de la unidad medica a: {} ", urlProperti);
         URL url = new URL(urlProperti + "unidad-medica/" + idUnidadMedica);
         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrió un error en la configuración de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener los detalles de la unidad medica");
                  log.info("getUnidadMedicaByid() - Ocurrió un error en los reportes al obtener los detalles de la unidad médica - error: {}", response);
                  throw consE;
               }
            } else {
               log.info("getUnidadMedicaByid() - Se obtuvieron los detalles de la unidad médica exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener la informacion de la unidad medica para los formatos impreso");
         log.error("Ocurrio un error inesperado al obtener los detalles de la unidad medica para los reportes - error: ", e);
         throw consE;
      }
   }

   public String getImgColor(Long gid, String color) throws ConsultaException {
      try {
         URL url = new URL(urlProperti + "groups-crud/image?gid=" + gid + "&color=" + color);

         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion ", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener información ");
                  log.info("getImgColor() - Ocurrio un error en obtener información - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getImgColor() - Se obtuvo información");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               return response.toString().replaceFirst("^data:image/png;base64,", "");
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado ", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No se pudo obtener información ");
         log.info("getImgColor() - Ocurrio un error en obtener información- error: {}", e);
         throw consE;
      }
   }

   public Map<String, Object> getMedicoFirmaByIdFirma(String idFirma) throws ConsultaException {
      try {
         URL url = new URL(urlProperti + "medicos/firma/" + idFirma);

         Integer contador = 0;
         Boolean ciclo = true;
         do {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            Map<String, Object> infoTocken = obtenerToken();
            token = "bearer " + (String) infoTocken.get("access_token");
            conn.setRequestProperty("Authorization", token);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               response.toString();
               contador++;
               ciclo = true;
               if (contador > 3) {
                  ConsultaException consE = new ConsultaException("Ocurrio un error en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
                  consE.addError("No se pudo obtener información de la firma del médico");
                  log.info("getMedicoFirmaByIdFirma() - Ocurrio un error en obtener información de la firma del médico - error: {}", response.toString());
                  throw consE;
               }
            } else {
               log.info("getMedicoFirmaByIdFirma() - Se obtuvo información de la firma del medico exitosamente");
               ciclo = false;
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String currentLine;
               while ((currentLine = br.readLine()) != null) {
                  response.append(currentLine);
               }
               br.close();
               Map<String, Object> JsonResponse = mapp.readValue(response.toString(), Map.class);
               return JsonResponse;
            }
         } while (ciclo);
         return null;
      } catch (ConsultaException ce) {
         throw ce;
      } catch (Exception e) {
         ConsultaException consE = new ConsultaException("Ocurrio un error inesperado en la configuraion de los reportes", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No se pudo obtener información de la firma del médico");
         log.info("getMedicoFirmaByIdFirma() - Ocurrio un error en obtener información de la firma del médico - error: {}", e);
         throw consE;
      }
   }
}
