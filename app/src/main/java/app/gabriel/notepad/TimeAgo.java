import android.content.Context;

public class TimeAgo {



            private static final int seconds = 1000;
            private  static final int minutes = 60 * seconds;
    private static  final int hours = 60 * minutes;
    private static final int days = 24 * hours;
+
        +    public  static String getTimeAgo(long time, Context context){
            +
                    +        if(time < 1000000000000l){
                +            time += 1000;
                +
                        +        }
            +        long now = System.currentTimeMillis();
            +
                    +        if(time > now || time <= 0) {
                +                return  null;
                +        }
            +
                    +        final long diff = now - time;
            +
                    +        if(diff < minutes){
                +            return  "Adicionado agora";
                +        }
            +        else if(diff < 2 * minutes){
                +            return "um minuto atrás";
                +        }
            +        else if(diff < 50 * minutes){
                +            return ((diff / minutes) + " minutos atrás");
                +        }
            +        else if(diff < 90 * minutes){
                +            return ("Uma Horas atrás");
                +        }
            +        else if(diff < 24 * hours){
                +            return  ((diff / hours) + " horas atrás");
                +        }
            +        else if(diff < 48 * hours) {
                +            return "Ontem";
                +        }
            +        else {
                +            return diff / days  + " dias atrás";
                +        }
            +    }
}

