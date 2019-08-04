package styles.zonetech.net.styles.Server;


import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Client {

    public static Retrofit client=null;
    public static Retrofit getClient(String Base_Url){
        if(client==null)
        {
            client= new Retrofit.Builder().
                    baseUrl(Base_Url).
                    addConverterFactory(ScalarsConverterFactory.create()).
                    build();
        }
        return client;
    }


}
