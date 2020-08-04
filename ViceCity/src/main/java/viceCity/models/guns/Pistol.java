package viceCity.models.guns;

public class Pistol extends BaseGun {
    private static final int BULLETS_PER_BARREL = 10;
    private static final int TOTAL_BULLETS = 100;
    public Pistol(String name) {
        super(name, BULLETS_PER_BARREL, TOTAL_BULLETS);
    }

    @Override
    public int fire() {
        setBulletsPerBarrel(getBulletsPerBarrel() - 1);
        if(getBulletsPerBarrel() == 0){
            if(getTotalBullets() >= 10) {
                setBulletsPerBarrel(10);
                setTotalBullets(getTotalBullets() - 10);
            }else{
                int size = getTotalBullets();
                setBulletsPerBarrel(size);
                setTotalBullets(0);
            }
        }
        return 1;
    }
}
