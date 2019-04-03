/**
 *
 * @author Sirn William
 */
import java.util.ArrayList;


/**
 * This holds the values for a Block in the Blockchain, and it has methods to compute the Merkle Root and generate a hash.
 */
public class Block {


    private String sMerkleRoot;
    private int iDifficulty = 5; // Mining seconds in testing 5: 6,10,15,17,20,32 | testing 6: 12,289,218
    private String sNonce;
    private String sMinerUsername;
    private String sHash;

    
    
//    public String computeMerkleRoot(ArrayList<String> items){
//        if (items.size() < 2){
//            return null;
//        }else if (items.size() == 2){
//            String item1 = items.get(0);
//            String item2 = items.get(1);
//            // + indicates a hash code has been done.
//            if (!items.get(0).startsWith("+")){
//                return BlockchainUtil.generateHash(BlockchainUtil.generateHash(items.get(0)) + BlockchainUtil.generateHash(items.get(1)));
//            }else{
//                return BlockchainUtil.generateHash(items.get(0).substring(1) + items.get(1).substring(1));
//            }
//            
//        }else{
//            ArrayList<String> newItems = new ArrayList<>();
//            for (int itemIndex = 0; itemIndex < items.size(); itemIndex += 2){
//                if (!items.get(itemIndex).startsWith("+")){
//                    newItems.add("+" + BlockchainUtil.generateHash(BlockchainUtil.generateHash(items.get(itemIndex)) + BlockchainUtil.generateHash(items.get(itemIndex + 1))));
//                }else{
//                    newItems.add("+" + BlockchainUtil.generateHash(items.get(itemIndex).substring(1) + items.get(itemIndex+1).substring(1)));
//                }
//            }
//            return computeMerkleRoot(newItems);
//        }
//    }
    
    
    
    /**
     * This computes the Merkle Root. It either accepts 2 or 4 items, or if made to be dynamic, then accepts any
     * multiple of 2 (2,4,8.16.32,etc.) items.
     * @param lstItems
     * @return
     */
   
    public String computeMerkleRoot(ArrayList<String> lstWord) {
        MerkleNode mNode0 = new MerkleNode();
        MerkleNode mNode1 = new MerkleNode();
        MerkleNode mNode2 = new MerkleNode();
        ArrayList<String> lstItem = new ArrayList<String>();
        lstItem.add("true");
        if (lstWord.get(0).equals("true")) {
            for (int i = 1; i < lstWord.size(); i += 2) {
                mNode0.sHash = lstWord.get(i);
                mNode1.sHash = lstWord.get(i + 1);
                populateMerkleNode(mNode2, mNode0, mNode1);
                lstItem.add(mNode2.sHash);
            }
            if (lstItem.size() == 2) {
                populateMerkleNode(mNode2, mNode0, mNode1);
                return mNode2.sHash;
            }
            return computeMerkleRoot(lstItem);
        } else {
            for (int i = 0; i < lstWord.size(); i += 2) {
                mNode0.sHash = BlockchainUtil.generateHash(lstWord.get(i));
                mNode1.sHash = BlockchainUtil.generateHash(lstWord.get(i + 1));
                populateMerkleNode(mNode2, mNode0, mNode1);
                lstItem.add(mNode2.sHash);
            }
            if (lstItem.size() == 2) {
                populateMerkleNode(mNode2, mNode0, mNode1);
                return mNode2.sHash;
            } else {
                return computeMerkleRoot(lstItem);
            }
        }
    }

    /**
     * This method populates a Merkle node's left, right, and hash variables.
     * @param oNode
     * @param oLeftNode
     * @param oRightNode
     */
    private void populateMerkleNode(MerkleNode oNode, MerkleNode oLeftNode, MerkleNode oRightNode){
        
        oNode.sHash = new BlockchainUtil().generateHash(oLeftNode.sHash + oRightNode.sHash);
        oNode.oLeft = oLeftNode;
        oNode.oRight = oRightNode;
    }


    // Hash this block, and hash will also be next block's previous hash.

    /**
     * This generates the hash for this block by combining the properties and hashing them.
     * @return
     */
    public String computeHash() {
        return new BlockchainUtil().generateHash(sMerkleRoot + iDifficulty + sMinerUsername + sNonce);
    }



    public int getDifficulty() {
        return iDifficulty;
    }


    public String getNonce() {
        return sNonce;
    }
    public void setNonce(String nonce) {
        this.sNonce = nonce;
    }

    public void setMinerUsername(String sMinerUsername) {
        this.sMinerUsername = sMinerUsername;
    }

    public String getHash() { return sHash; }
    public void setHash(String h) {
        this.sHash = h;
    }

    public synchronized void setMerkleRoot(String merkleRoot) { this.sMerkleRoot = merkleRoot; }




    /**
     * Run this to test your merkle tree logic.
     * @param args
     */
    public static void main(String[] args){

        ArrayList<String> lstItems = new ArrayList<>();
        Block oBlock = new Block();
        String sMerkleRoot;

        // These merkle root hashes based on "t1","t2" for two items, and then "t3","t4" added for four items.
        String sExpectedMerkleRoot_2Items = "3269f5f93615478d3d2b4a32023126ff1bf47ebc54c2c96651d2ac72e1c5e235";
        String sExpectedMerkleRoot_4Items = "e08f7b0331197112ff8aa7acdb4ecab1cfb9497cbfb84fb6d54f1cfdb0579d69";

        lstItems.add("t1");
        lstItems.add("t2");


        // *** BEGIN TEST 2 ITEMS ***

        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_2Items)){

            System.out.println("Merkle root method for 2 items worked!");
        }

        else{
            System.out.println("Merkle root method for 2 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_2Items);
            System.out.println("Received: " + sMerkleRoot);

        }


        // *** BEGIN TEST 4 ITEMS ***

        lstItems.add("t3");
        lstItems.add("t4");
        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_4Items)){

            System.out.println("Merkle root method for 4 items worked!");
        }

        else{
            System.out.println("Merkle root method for 4 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_4Items);
            System.out.println("Received: " + sMerkleRoot);

        }
    }
}

