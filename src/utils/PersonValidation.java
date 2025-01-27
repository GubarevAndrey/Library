package utils;

public class PersonValidation {
    private static  String special="!%@$&*()[]";
    public static boolean isEmailValid(String email) {
        //1.
        int index= email.indexOf("@");
        if(index==-1 || index != email.lastIndexOf("@")) return false;

        //2.
        if(email.indexOf(".",index+2)==-1)  return false;

        //3.
        if(email.lastIndexOf(".")>=email.length()-2) return false;

        //4.
        for (int i=0;i<email.length();i++){
            char ch= email.charAt(i);
            if(!(Character.isAlphabetic(ch)
                    || Character.isDigit(ch)
                    || ch=='_' || ch=='-' || ch=='.' || ch =='@')){
                return false;
            }
        }

        //5.
        if(index==0 ) return false;

        //6.
        if(! Character.isAlphabetic(email.charAt(0))) return false;

        return true;
    }



    /*

    Требования к паролю:
    1. Длина больше или равна 8 символов
    2. должна быть минимум одна маленькая буква
    3. должна быть минимум одна большая буква
    4.  должна быть минимум одна цифра
    5. должен быть минимум 1 спецсимвол ( !%@$&*()[] )
     */
    public static boolean isPasswordValid(String password) {
        boolean isLow=false;
        boolean isUpp=false;
        boolean isDig=false;
        boolean isSpec=false;
        char ch;
        if(password==null || password.length()<8) {
            return false;
        }
        for(int i=0;i<password.length();i++) {
            ch=password.charAt(i);
            if(Character.isLowerCase(ch)) isLow=true;
            if(Character.isUpperCase(ch)) isUpp=true;
            if(Character.isDigit(ch)) isDig=true;
            if (special.indexOf(ch)!=-1) isSpec=true;
        }
        return isLow && isUpp && isDig && isSpec;
    }
}
