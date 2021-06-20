package com.example.permisosunsada;

public class PEDIR_URL {
    private static String HOST = "http://areco.gob.ar:9528/";

    private static String peticionEdificios = "api/edificio/all";
    private static String peticionQR = "api/solicitud/find/uuid/";
    private static String updateSolicitud = "/api/solicitud/update/";

    public static String UpdateSolicitud(String id_solicitud, int idEdificio){
        return HOST + updateSolicitud + id_solicitud + "/" + idEdificio;
    }

    public static String PeticionQR(String qr){
        return HOST+peticionQR+qr;
    }

    public static String PeticionEdificios(){
        return HOST+peticionEdificios;
    }
}
