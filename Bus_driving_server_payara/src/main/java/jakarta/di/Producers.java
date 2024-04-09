package jakarta.di;

import com.google.gson.Gson;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class Producers {


    @Produces
    public Jsonb producesJsonb() {
        return JsonbBuilder.create();
    }

    @Produces
    public Gson producesGson() {
        return new Gson();
    }

}
