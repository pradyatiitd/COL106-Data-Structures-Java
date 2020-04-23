import java.util.*;

class Pair<Key extends Comparable<Key>,Value>{
    public Key key;
    public Value val;

    public Pair(Key k, Value v){
        this.key = k;
        this.val = v;
    }

    public String toString(){
        return this.key.toString() + "=" + this.val.toString();
    }
}

class Node{
    public int t;
    public int currKeys;
    public ArrayList<Pair> currNodes;
    public ArrayList<Node> childNodes;

    public Node(int t){
        this.t = t;
        this.currKeys = 0;
        this.currNodes = new ArrayList<Pair>();
        this.childNodes = new ArrayList<Node>();
    }

    public void set(int idx,ArrayList<Pair> list,Pair p){
        if(idx==list.size()){
            list.add(p);
        }else{
            list.set(idx,p);
        }
    }

    public void set(int idx,ArrayList<Node> list,Node n){
        if(idx==list.size()){
            list.add(n);
        }else{
            list.set(idx,n);
        }
    }

    public String toString(){
        String ans = "[";
        
        if(this.childNodes.size()==0){
            for(int i = 0;i<this.currKeys;i++){
                ans+=this.currNodes.get(i).toString();
                if(i!=this.currKeys-1){
                    ans+=",";
                }
            }
        }else{
            for(int i = 0;i<this.currKeys;i++){
                ans+=this.childNodes.get(i).toString();
                ans+=",";
                ans+=this.currNodes.get(i).toString();
                ans+=",";
            }
            ans+=this.childNodes.get(this.currKeys).toString();
        }
        
        ans+="]";
        return ans;
    }

    public void search(Pair p,List<Pair> valList){
        int temp = 0;
        if(this.childNodes.size()==0){
            for(int i = 0;i<this.currKeys;i++){
                temp = this.currNodes.get(i).key.compareTo(p.key);
                if(temp==0){
                    valList.add(this.currNodes.get(i));
                }else if(temp>0){
                    return;
                }
            }
        }else{
            for(int i = 0;i<this.currKeys;i++){
                temp = this.currNodes.get(i).key.compareTo(p.key);
                if(temp>0){
                    this.childNodes.get(i).search(p,valList);
                    return;
                }else if(temp==0){
                    this.childNodes.get(i).search(p,valList);
                    valList.add(this.currNodes.get(i));
                }
            }
            this.childNodes.get(this.currKeys).search(p,valList);
        }
    }

    public void insertNotFull(Pair p){
        int idx = this.currKeys-1;
        
        if(this.childNodes.size()==0){
            while(idx>=0 && this.currNodes.get(idx).key.compareTo(p.key)>0){
                this.set(idx+1,this.currNodes,this.currNodes.get(idx));

                idx--;
            }

            this.set(idx+1,this.currNodes,p);
            this.currKeys++;
        }else{
            while(idx>=0 && this.currNodes.get(idx).key.compareTo(p.key)>0){
                idx-=1;
            }

            if(this.childNodes.get(idx+1).currKeys==2*this.t-1){
                this.splitChild(idx+1,this.childNodes.get(idx+1));

                if(this.currNodes.get(idx+1).key.compareTo(p.key)<0){
                    idx++;
                }
            }
            this.childNodes.get(idx+1).insertNotFull(p);
        }
    }

    public void splitChild(int idx,Node node){
        Node n = new Node(this.t);
        n.currKeys = this.t-1;

        for(int i = 0;i<t-1;i++){
            n.currNodes.add(node.currNodes.get(i+this.t));
        }

        if(node.childNodes.size()!=0){
            for(int i = 0;i<t;i++){
                n.childNodes.add(node.childNodes.get(i+this.t));
            }
        }
        node.currKeys = this.t-1;

        for(int i = this.currKeys;i>=idx+1;i--){
            this.set(i+1,this.childNodes,this.childNodes.get(i));

        }
        this.set(idx+1,this.childNodes,n);

        for(int i = this.currKeys-1;i>=idx;i--){
            this.set(i+1,this.currNodes,this.currNodes.get(i));

        }
        this.set(idx,this.currNodes,node.currNodes.get(this.t-1));

        this.currKeys++;
    }

    public void removeFromLeaf(int idx){
        for(int i = idx+1;i<this.currKeys;i++){
            this.currNodes.set(i-1,this.currNodes.get(i));
        }
        this.currKeys-=1;
    }

    public void removeFromNonLeaf(int idx){
        Pair p1 = this.currNodes.get(idx);

        if(this.childNodes.get(idx).currKeys>=this.t){
            Pair p2 = this.getPred(idx);
            this.set(idx,this.currNodes,p2);
            this.childNodes.get(idx).delete(p2);
        }else if(this.childNodes.get(idx+1).currKeys>=this.t){
            Pair p3 = this.getSucc(idx);
            this.set(idx,this.currNodes,p3);
            this.childNodes.get(idx+1).delete(p3);
        }else{
            this.merge(idx);
            this.childNodes.get(idx).delete(p1);
        }
    }

    public Pair getPred(int idx){
        Node child = this.childNodes.get(idx);
        while(child.childNodes.size()!=0){
            child = child.childNodes.get(child.currKeys);
        }

        return child.currNodes.get(child.currKeys-1);
    }

    public Pair getSucc(int idx){
        Node child = this.childNodes.get(idx+1);
        while(child.childNodes.size()!=0){
            child = child.childNodes.get(0);
        }

        return child.currNodes.get(0);
    }

    public void borrowFromPrev(int idx){
        Node curr = this.childNodes.get(idx);
        Node sibling = this.childNodes.get(idx-1);

        for(int i = curr.currKeys-1;i>=0;i--){
            this.set(i+1,curr.currNodes,curr.currNodes.get(i));
        }

        if(curr.childNodes.size()!=0){
            for(int i = curr.currKeys;i>=0;i--){
                this.set(i+1,curr.childNodes,curr.childNodes.get(i));
            }
        }

        this.set(0,curr.currNodes,this.currNodes.get(idx-1));

        if(sibling.childNodes.size()!=0){
            this.set(0,curr.childNodes,sibling.childNodes.get(sibling.currKeys));
        }

        this.set(idx-1,this.currNodes,sibling.currNodes.get(sibling.currKeys-1));
        
        curr.currKeys++;
        sibling.currKeys--;
    }

    public void borrowFromNext(int idx){
        Node curr = this.childNodes.get(idx);
        Node sibling = this.childNodes.get(idx+1);

        this.set(curr.currKeys,curr.currNodes,this.currNodes.get(idx));

        if(curr.childNodes.size()!=0){
            this.set(curr.currKeys+1,curr.childNodes,sibling.childNodes.get(0));
        }

        this.set(idx,this.currNodes,sibling.currNodes.get(0));

        for(int i = 1;i<sibling.currKeys;i++){
            this.set(i-1,sibling.currNodes,sibling.currNodes.get(i));
        }

        if(sibling.childNodes.size()!=0){
            for(int i = 1;i<=sibling.currKeys;i++){
                this.set(i-1,sibling.childNodes,sibling.childNodes.get(i));
            }
        }
        
        curr.currKeys++;
        sibling.currKeys--;
    }

    public void merge(int idx){
        Node child = this.childNodes.get(idx);
        Node sibling = this.childNodes.get(idx+1);

        this.set(this.t-1,child.currNodes,this.currNodes.get(idx));
        
        for(int i = 0;i<sibling.currKeys;i++){
            this.set(i+this.t,child.currNodes,sibling.currNodes.get(i));
        }

        if(child.childNodes.size()!=0){
            for(int i = 0;i<=sibling.currKeys;i++){
                this.set(i+this.t,child.childNodes,sibling.childNodes.get(i));
            }
        }

        for(int i = idx+1;i<this.currKeys;i++){
            this.set(i-1,this.currNodes,this.currNodes.get(i));
        }

        for(int i = idx+2;i<=this.currKeys;i++){
            this.set(i-1,this.childNodes,this.childNodes.get(i));
        }

        child.currKeys+=(sibling.currKeys+1);
        this.currKeys-=1;
    }

    public void fill(int idx){
        if(idx!=0 && this.childNodes.get(idx-1).currKeys>=this.t){
            this.borrowFromPrev(idx);
        }else if(idx!=this.currKeys && this.childNodes.get(idx+1).currKeys>=this.t){
            this.borrowFromNext(idx);
        }else if(idx!=this.currKeys){
            this.merge(idx);
        }else{
            this.merge(idx-1);
        }
    }

    public int findKey(Pair p){
        int i = 0;
        while(i<this.currKeys && this.currNodes.get(i).key.compareTo(p.key)<0){
            i++;
        }
        return i;
    }

    public boolean delete(Pair p){
        int idx = this.findKey(p);

        if(idx<this.currKeys && this.currNodes.get(idx).key.compareTo(p.key)==0){
            if(this.childNodes.size()==0){
                this.removeFromLeaf(idx);
            }else{
                this.removeFromNonLeaf(idx);
            }
        }else if(this.childNodes.size()==0){
            return false;
        }else{
            boolean flag = (idx==this.currKeys);

            if(this.childNodes.get(idx).currKeys<this.t){
                this.fill(idx);
            }

            if(flag && idx>this.currKeys){
                return this.childNodes.get(idx-1).delete(p);
            }else{
                return this.childNodes.get(idx).delete(p);
            }
        }

        return true;
    }
}

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value>{
    public Node root;
    public int size;
    public int b;

    public BTree(int b) throws bNotEvenException{
        if(b%2==1){
            throw new bNotEvenException();
        }
        this.b = b;
        this.size = 0;
        this.root = new Node(b/2);
    }

    @Override
    public boolean isEmpty(){
        return this.size==0;
    }

    @Override
    public int size(){
        return this.size;
    }

    @Override
    public int height(){
        int height = 0;
        Node n = this.root;

        if(n==null){
            return -1;
        }

        while(n.childNodes.size()>0){
            n = n.childNodes.get(0);
            height++;
        }

        return height;
    }

    public String toString(){
        if(this.root!=null){
            return this.root.toString();
        }
        return "";
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException{
        if(this.root==null){
            throw new IllegalKeyException();
        }

        List<Value> valueList = new LinkedList<Value>();
        List<Pair> pairList = new LinkedList<Pair>();
        Pair p = new Pair(key,"value");
        this.root.search(p,pairList);

        if(pairList.size()==0){
            throw new IllegalKeyException();
        }
        
        for(Pair pair: pairList){
            valueList.add((Value)pair.val);
        }

        return valueList;
    }

    @Override
    public void insert(Key key, Value val){
        Pair p = new Pair(key,val);
        if(this.root.currNodes.size()==0){
            this.root.currNodes.add(p);
            this.root.currKeys+=1;
        }else if(this.root.currNodes.size()==this.b-1){
            Node n = new Node(this.root.t);
            n.childNodes.add(this.root);
            n.splitChild(0,this.root);

            int idx = 0;
            if(n.currNodes.get(0).key.compareTo(p.key)<0){
                idx++;
            }
            n.childNodes.get(idx).insertNotFull(p);

            this.root = n;
        }else{
            this.root.insertNotFull(p);
        }
        this.size+=1;
    }

    @Override
    public void delete(Key key) throws IllegalKeyException{
        if(this.root==null){
            throw new IllegalKeyException();
        }

        int occurences = 0;
        boolean stillPresent = true;

        while(this.root.currKeys>0 && stillPresent){
            stillPresent = this.root.delete(new Pair(key,"value"));
            occurences++;
            if(occurences==1 && stillPresent==false){
                throw new IllegalKeyException();
            }

            if(this.root.currKeys==0 && this.root.childNodes.size()>0){
                this.root = this.root.childNodes.get(0);
            }
        }
    }
}