package com.example.emediconn.Database;

public class AppConfig {
    public static String Status="status";

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

    public static String URL_GETDOCTORLIST= "http://healthcare.blucorsys.in/api/other/getDoctorByCategory.php";

    public static String URL_GETDOCTORDISCRIPTION= "http://healthcare.blucorsys.in/api/other/getDoctorDetails.php";

    public static String URL_TIMESLOT= "http://healthcare.blucorsys.in/api/other/getDoctorAppointment.php";

    public static String URL_BOOKAPPOINTMENT= "http://healthcare.blucorsys.in/api/other/bookDoctorAppointment.php";

    public static String URL_GETPATIENTAPPOINTMENT= "http://healthcare.blucorsys.in/api/other/getDoctorsDailyAppointments.php";

    public static String URL_GETMYAPPOINTMENT= "http://healthcare.blucorsys.in/api/other/getPatientAppointments.php";

    public static String URL_GETVideoConsult= "http://healthcare.blucorsys.in/api/other/getDoctorsVideoAppointments.php";

    public static String URL_GETVIDEOAPPOINTMENT= "http://healthcare.blucorsys.in/api/other/getPatientVideoAppointments.php";

    public static String URL_GETPATIENTPROFILE= "http://healthcare.blucorsys.in/api/patient/getPatientDetails.php";

    public static String URL_UPDATEPATIENT= "http://healthcare.blucorsys.in/api/patient/updatePatientDetails.php";

    public static String URL_UPDATEPROFILEPIC= "http://healthcare.blucorsys.in/api/patient/updatePatientProfilePicture.php";

    public static String URL_CHANGEPASSWORD= "http://healthcare.blucorsys.in/api/user/changePasswordAPI.php";

    public static String URL_RESETPASSWORD= "http://healthcare.blucorsys.in/api/user/forgotPasswordAPI.php";


}
