package vn.com.insee.corporate.util.insee;

public class INSEEUtil {

    public static INSEEMessage getMessageFromBuyingINSEECement(int productId, int bag) throws INSEEProductNotExitException {
        int weigh = weigh(bag, productId);
        int saving = reduceEmissions(productId, weigh / 1000);
        int numOfTree = numberOfTree(saving);
        return new INSEEMessage(numOfTree, saving);
    }

    public static int numberOfTree(int savingVolume) {
        return savingVolume / DataManager.EVERY_TREE_HELP_TO_REDUCE_CO2;
    }

    public static int reduceEmissions(int productId, int ton) throws INSEEProductNotExitException {
        Product product = DataManager.findById(productId);
        if (product == null) {
            throw new INSEEProductNotExitException();
        }
        return reduceEmissions(product, ton);
    }

    public static int reduceEmissions(Product product, int ton) {
        int saving = product.getCommonVolumeExhaust() - product.getInseeVolumeExhaust();
        return ton * saving;
    }

    public static int weigh(int bag, int productId) throws INSEEProductNotExitException {
        Product product = DataManager.findById(productId);
        if (product == null) {
            throw new INSEEProductNotExitException();
        }
        return product.getVolumeOneBag() * bag;
    }


}
