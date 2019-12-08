package com.gaminho.theshowapp.properties;

public class GoogleDriveProperty {

    private static final PropertyHelper helper = new PropertyHelper("drive.properties");

    public static String ROOT_FOLDER = readProperty("root_folder");
    public static String SHOW_FOLDER = readProperty("the_show_folder");
    public static String GUESTS_FOLDER = readProperty("guests_folder");
    public static String GUEST_FOLDER_ID = readProperty("guests_folder_id");
    public static String RESOURCES_FOLDER = readProperty("resources_folder");
    public static String RESOURCES_FOLDER_ID = readProperty("resources_folder_id");

    public static String getGuestFolderId(){
        return helper.readProperty("guests_folder_id");
    }

    public static String getResourcesFolderId(){
        return helper.readProperty("resources_folder_id");
    }


    public static String readProperty(String propertyKey){
        return helper.readProperty(propertyKey);
    }

    public static void setGuestFolderId(String id){
        helper.writeProperty("guests_folder_id", id);
    }

    public static void setResourcesFolderId(String id){
        helper.writeProperty("resources_folder_id", id);
    }

    public static void setGamesFileId(String id){
        helper.writeProperty("resources_folder_id", id);
    }
}
