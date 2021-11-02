package quarkus.world.tour;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.Duration;
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
        return Band.listAlive();
    }

    @GET
    @Path("{id}")
    public Uni<Band> listAlive(@PathParam("id") long id) {
       return Band.<Band>findById(id).onItem().ifNull().failWith(() -> {
            Response notFound = Response.status(Response.Status.NOT_FOUND)
                .entity("no band found with id: " + id)
                .build();
            return new WebApplicationException(notFound);
        });
    }

    @POST
    public Uni<RestResponse<Void>> listAlive(Band band) {
        return Panache.withTransaction(band::persistAndFlush)
                .replaceWith(band)
                .onItem()
                .transform(b -> RestResponse.created(UriBuilder.fromUri("/rock/" + b.id).build()));
    }
}