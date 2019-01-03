package fxlauncher.emasters.utils;

/**
 * Calculates the download rate speed.
 **/
public class DownloadRate {

    private long lastPacketTime = 0;
    private double lastPacketSize = 0;
    private String lastArchive = "";

    public double mbPerSecond = 0;
    public double secondsToComplete = 0;

    /**
     * Calculates the download speed based on the total MB and current MB and the archive being downloaded.
     *
     * @param currentMB the current size of MB being downloaded
     * @param totalMB the total MB to download
     * @param archive the archive being downloaded
     *
     * @return true if needs to update the view, false otherwise
     **/
    public boolean calculateDownloadSpeed(double currentMB, double totalMB, String archive) {
        if (lastPacketTime == 0) {
            lastPacketTime = System.currentTimeMillis();
            lastPacketSize = currentMB;
            lastArchive = archive;
            return false;
        }

        if (!lastArchive.equals(archive)){
            lastPacketTime = System.currentTimeMillis();
            lastPacketSize = currentMB;
            lastArchive = archive;
            return false;
        }

        long nowMillis = System.currentTimeMillis();
        long timeDiff = nowMillis - lastPacketTime;

        if (timeDiff < 1000) {
            return false;
        }

        double sizeDiff = currentMB - lastPacketSize;

        if (sizeDiff == 0) {
            return false;
        }

        mbPerSecond = (sizeDiff / timeDiff) * 1000;
        secondsToComplete = (totalMB - currentMB) / mbPerSecond;

        lastPacketTime = nowMillis;
        lastPacketSize = currentMB;

        return true;
    }
}
