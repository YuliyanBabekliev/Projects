package viceCity.models.guns;

public class Rifle extends BaseGun {
    private static final int BULLETS_PER_BARREL = 50;
    private static final int TOTAL_BULLETS = 500;
    public Rifle(String name) {
        super(name, BULLETS_PER_BARREL, TOTAL_BULLETS);
    }

    @Override
    public int fire() {
        setBulletsPerBarrel(getBulletsPerBarrel() - 5);
        if(getBulletsPerBarrel() <= 0){
            if(getTotalBullets() >= 50) {
                setBulletsPerBarrel(50);
                setTotalBullets(getTotalBullets() - 50);
            }else{
                int size = getTotalBullets();
                setBulletsPerBarrel(size);
                setTotalBullets(0);
            }
        }
        return 5;
    }
}
