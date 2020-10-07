
import org.json.JSONObject;
import mytools.MyArrays;
import org.json.JSONArray;

public class test {

    public static void main(String[] args) {

        JSONObject json = new JSONObject(MyArrays.getSubUnities());

        try {
            JSONArray jsonA = new JSONArray("[\"\",\"Cdo Ser\",\"Bda Mil\",\"Ca A\",\"Ca B\",\"Ca C\",\"Ca E\"]");
            System.out.println(jsonA);

            for (int i = 0; i < jsonA.length(); i++) {
                 System.out.println(jsonA.getString(i));
            }
            
            jsonA.put(MyArrays.getAptitude());
            System.out.println(jsonA);

        } catch (Exception e) {
            System.out.println(e);
        }

        

    }
}
