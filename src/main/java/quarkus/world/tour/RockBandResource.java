package quarkus.world.tour;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/rock")
public class RockBandResource {

    @GET
    public Uni<List<Band>> listBands() {
        return Band.listAll();
    }

    @GET
    @Path("alive")
    public Uni<List<Band>> listAlive() {
        return null;
    }

    @GET
    @Path("{id}")
    public Uni<Band> listAlive(@PathParam("id") long id) {
       return null;
    }

    @POST
    public Uni<RestResponse<Void>> createBand(Band band) {
        return null;
    }
}