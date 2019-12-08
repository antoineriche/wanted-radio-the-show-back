package com.gaminho.theshowapp.service;

import com.gaminho.theshowapp.dao.BeatboxerRepository;
import com.gaminho.theshowapp.dao.RapperRepository;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.model.artist.BeatBoxer;
import com.gaminho.theshowapp.model.artist.Rapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArtistService {

    private static Logger log = LoggerFactory.getLogger(ArtistService.class);

    @Autowired
    private RapperRepository rapperRepository;

    @Autowired
    private BeatboxerRepository beatboxerRepository;


    public List<Rapper> getAllRappers(){
        return rapperRepository.findAll();
    }

    private <T> Optional<T> getArtistByName(List<T> allArtists, String name){
        return allArtists.stream()
                .filter(artist -> {
                    if(artist instanceof Rapper){
                        return ((Rapper) artist).getArtistDetails().getArtistName().equalsIgnoreCase(name);
                    } else if(artist instanceof BeatBoxer){
                        return ((BeatBoxer) artist).getArtistDetails().getArtistName().equalsIgnoreCase(name);
                    } else {
                        return false;
                    }
                }).findFirst();
    }

    public Optional<Rapper> getRapperByName(String artistName){
        return getArtistByName(getAllRappers(), artistName);
    }

    public Optional<BeatBoxer> getBeatBoxerByName(String artistName){
        return getArtistByName(getAllBeatBoxers(), artistName);
    }

    public List<BeatBoxer> getAllBeatBoxers(){
        return beatboxerRepository.findAll();
    }

    /**
     * @param artistType the artist type
     * @param artistName the artist name
     * @return true if artist with same name and type is already in base, false otherwise.
     */
    public boolean artistExists(@NotNull final ArtistType artistType,
                                   @NotNull final String artistName){
        return (artistType == ArtistType.BEATBOXER && beatBoxerExists(artistName))
                || (artistType == ArtistType.RAPPER && rapperExists(artistName));

    }

    /**
     *
     * @param rapperName the name to match
     * @return true if a rapper with same name already exists, false otherwise
     */
    public boolean rapperExists(@NotNull final String rapperName){
        return getRapperByName(rapperName).isPresent();
    }

    /**
     *
     * @param beatBoxerName the name to match
     * @return true if a beat boxer with same name already exists, false otherwise
     */
    public boolean beatBoxerExists(@NotNull final String beatBoxerName){
        return getBeatBoxerByName(beatBoxerName).isPresent();
    }

    /**
     * Save a new Rapper
     * @param rapper the Rapper to save
     * @return the saved Rapper
     */
    public Rapper saveNewArtist(@NotNull final Rapper rapper){
        return rapperRepository.save(rapper);
    }

    /**
     * Save a new BeatBoxer
     * @param beatBoxer the BeatBoxer to save
     * @return the saved BeatBoxer
     */
    public BeatBoxer saveNewArtist(@NotNull final BeatBoxer beatBoxer){
        return beatboxerRepository.save(beatBoxer);
    }
}
