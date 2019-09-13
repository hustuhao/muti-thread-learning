import java.util.*;
/*
dgbaechf  iorder 中序
gbdehfca  porder 后序
方便复制：
dgbaechf
gbdehfca

//样例输出
//adbgcefh

 */
class TreeNode{
    char val;
    TreeNode left;
    TreeNode right;
    public TreeNode(char val){
        this.val = val;
    }
}

public class test2{

    public static void preOrder(TreeNode root){
        if(root==null){
            return;
        }
        System.out.print(root.val);
        preOrder(root.left);
        preOrder(root.right);
    }
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String iorder = in.nextLine();
        String porder = in.nextLine();
        preOrder(fun(porder,iorder));
    }
    public static TreeNode fun(String porder,String iorder){
        TreeNode root = new TreeNode('0');
        if(porder.length()>0){
            int len = porder.length();
            root.val = porder.charAt(len-1);
            root.left = null;
            root.right = null;
            String p1,p2,in1,in2;//分成两个部分
            int index = iorder.indexOf(porder.charAt(len-1));
            in1 = iorder.substring(0,index); // (不包括index)
            in2 = iorder.substring(index+1);
            p1=porder.substring(0,index);
            p2=porder.substring(index,porder.length()-1);
            root.left  = fun(p1,in1);
            root.right = fun(p2,in2);
        }else{
            return null;
        }
        return root;

    }

}
