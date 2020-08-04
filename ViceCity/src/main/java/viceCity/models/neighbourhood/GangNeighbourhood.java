package viceCity.models.neighbourhood;

import viceCity.models.guns.Gun;
import viceCity.models.players.Player;
import viceCity.repositories.GunRepository;

import java.util.Collection;

public class GangNeighbourhood implements Neighbourhood {
    @Override
    public void action(Player mainPlayer, Collection<Player> civilPlayers) {
        for (Gun gun : mainPlayer.getGunRepository().getModels()) {
            while (gun.getTotalBullets() <= 0){
                for (Player civilPlayer : civilPlayers) {
                    while (civilPlayer.isAlive() || gun.getTotalBullets() <= 0){
                        if(gun.getClass().getSimpleName().equals("Pistol")){
                            civilPlayer.takeLifePoints(1);
                        }else if(gun.getClass().getSimpleName().equals("Rifle")){
                            civilPlayer.takeLifePoints(5);
                        }
                        if(!civilPlayer.isAlive()){
                            civilPlayers.remove(civilPlayer);
                        }
                    }
                }
            }
        }

        for (Player civilPlayer : civilPlayers) {
            if(civilPlayer.isAlive()){
                for (Gun gun : civilPlayer.getGunRepository().getModels()) {
                    while (mainPlayer.isAlive() || gun.getTotalBullets() <= 0){
                        if(gun.getClass().getSimpleName().equals("Pistol")){
                            mainPlayer.takeLifePoints(1);
                        }else if(gun.getClass().getSimpleName().equals("Rifle")){
                            mainPlayer.takeLifePoints(5);
                        }
                    }
                }
            }
        }
    }
}
