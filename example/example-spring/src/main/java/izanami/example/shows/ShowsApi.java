package izanami.example.shows;

import akka.Done;
import io.vavr.collection.List;
import io.vavr.control.Option;
import izanami.FeatureEvent;
import izanami.example.shows.providers.betaserie.BetaSerieApi;
import izanami.example.shows.providers.tvdb.TvdbShowsApi;
import izanami.javadsl.FeatureClient;
import izanami.javadsl.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;
import static io.vavr.Predicates.*;

@Primary
@Component
public class ShowsApi implements Shows {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowsApi.class);

    private final TvdbShowsApi tvdbShowsApi;

    private final BetaSerieApi betaSerieApi;

public ShowsApi(TvdbShowsApi tvdbShowsApi, BetaSerieApi betaSerieApi) {
    this.tvdbShowsApi = tvdbShowsApi;
    this.betaSerieApi = betaSerieApi;
}

    @Override
    public List<ShowResume> search(String serie) {
        return tvdbShowsApi.search(serie);
    }

    @Override
    public Option<Show> get(String id) {
        return tvdbShowsApi.get(id);
    }
}
