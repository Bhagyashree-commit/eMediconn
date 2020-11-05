package com.example.emediconn.Database;

public class AppConfig {

    public static String BASE_URL="http://healthcare.blucorsys.in/api/user/";

    public static String URL_REGISTERPATIENT=BASE_URL+"register.php";

    public static String URL_LOGINPATIENT= BASE_URL+"login.php";

    public static String URL_FORGETPASSWORD=BASE_URL+"validateOTP.php";

    public static String URL_VALIDATEMOBILENUM=BASE_URL+"validateMobileNumber.php";

    public static String URL_GETSPECIALITYNAME="http://healthcare.blucorsys.in/api/other/getSpeciality.php";

    public static String URL_DOCTORPROFILE1="http://healthcare.blucorsys.in/api/doctor/updateDoctorPersonalInformation.php";

    public static String URL_DOCTORMEDICALREGISTRATION= "http://healthcare.blucorsys.in/api/doctor/updateDoctorMedicalRegistration.php";

    public static String URL_EDUDETAIL= "http://healthcare.blucorsys.in/api/doctor/updateDoctorEducation.php";

    public static String URL_HOSPDETAILS= "http://healthcare.blucorsys.in/api/doctor/updateDoctorHospitalDetails.php";

    public static String URL_UPLOADIDPROOF= "http://healthcare.blucorsys.in/api/doctor/uploadDocument.php";


}
