package com.eusecom.saminveantory;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * http://www.ala.sk/upload/index.php
 *
 *
 < ?php

 $file_name = $_POST["file_name"];
 $file_name=str_replace("\"","",$file_name);

 $f = fopen($file_name, "wb");
 fwrite($f,base64_decode($_POST['file']));
 fclose($f);

 if (File_Exists ("../upload/odberfak.xml"))
 {
 if (File_Exists ("../tmp/importfak17.xml"))
 {
 $soubor = unlink("../tmp/importfak17.xml");
 }
 copy("../upload/odberfak.xml", "../tmp/importfak17.xml");
 }

 $namex="file ".$file_name." uploaded file new_14";


 $response = array("login" => "200","name" => $namex);
 echo json_encode($response);

 ?>

 */


public interface UploadFileAPI {

    String ENDPOINT = "http://www.ala.sk/";

    @Multipart
    @POST("upload/index.php")
    Call<UploadFileResponse> upload(@Part("file_name") String file_name,
                                    @Part("file") RequestBody file);
}