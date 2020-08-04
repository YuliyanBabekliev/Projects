package viceCity.core;

import viceCity.core.interfaces.Controller;
import viceCity.models.guns.Gun;
import viceCity.models.guns.Pistol;
import viceCity.models.guns.Rifle;
import viceCity.models.neighbourhood.GangNeighbourhood;
import viceCity.models.players.CivilPlayer;
import viceCity.models.players.MainPlayer;
import viceCity.models.players.Player;
import viceCity.repositories.GunRepository;
import viceCity.repositories.interfaces.Repository;

import java.util.*;

import static viceCity.common.ConstantMessages.*;

public class ControllerImpl implements Controller {
    private Map<String, Player> players;
    private Deque<Gun> guns;
    public ControllerImpl() {
        this.players = new HashMap<>();
        Player mainPlayer = new MainPlayer();
        this.players.put(mainPlayer.getName(), mainPlayer);
        this.guns = new ArrayDeque<>();
    }

    @Override
    public String addPlayer(String name) {
        Player player = new CivilPlayer(name);
        this.players.put(player.getName(), player);
        return String.format(PLAYER_ADDED, name);
    }

    @Override
    public String addGun(String type, String name) {
        Gun gun;
        if(type.equals("Pistol")){
            gun = new Pistol(name);
            this.guns.offer(gun);
        }else if(type.equals("Rifle")){
            gun = new Rifle(name);
            this.guns.offer(gun);
        }else{
            return GUN_TYPE_INVALID;
        }
        return String.format(GUN_ADDED, name, type);
    }

    @Override
    public String addGunToPlayer(String name) {
        Player player;
        if(this.guns.size() == 0){
            return GUN_QUEUE_IS_EMPTY;
        }
        Gun first = guns.poll();

        if(name.equals("Vercetti")){
            players.get("Tommy Vercetti").getGunRepository().add(first);
            return String.format(GUN_ADDED_TO_MAIN_PLAYER, first.getName(), "Tommy Vercetti");
        }else if(this.players.containsKey(name)){
            players.get(name).getGunRepository().add(first);
            return String.format(GUN_ADDED_TO_CIVIL_PLAYER, first.getName(), name);
        }else{
            return CIVIL_PLAYER_DOES_NOT_EXIST;
        }
    }

    @Override
    public String fight() {
        GangNeighbourhood gangNeighbourhood = new GangNeighbourhood();
        List<Player> civilPlayers = new ArrayList<>();

        int deadCivilPlayers = 0;
        int mainPlayerLifePoint = 0;
        boolean isDead = false;

        for (Player value : players.values()) {
            if(value.isAlive()) {
                civilPlayers.add(value);
            }
        }

        gangNeighbourhood.action(players.get("Tommy Vercetti"), civilPlayers);

        for (Player value : players.values()) {
            if(!value.isAlive()){
                deadCivilPlayers++;
                isDead = true;
                civilPlayers.remove(value);
            }else{
                if(players.get(value.getName()).equals("Tommy Vercetti")){
                    mainPlayerLifePoint = value.getLifePoints();
                }
            }
        }
        StringBuilder sb = new StringBuilder();

        if(!isDead){
            return FIGHT_HOT_HAPPENED;
        }else{
            sb.append("A fight happened:")
                    .append(System.lineSeparator())
                    .append(String.format("Tommy live points: %d!", mainPlayerLifePoint))
                    .append(System.lineSeparator())
                    .append(String.format("Tommy has killed: %d players!", deadCivilPlayers))
                    .append(System.lineSeparator())
                    .append(String.format("Left Civil Players: %d!", civilPlayers.size()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
