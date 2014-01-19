package utils;



public class NonContinuousTable<T> {
	private T[] data;
	private	int dataCurs;
	private int size;
	private int sizeMax;
	
	public NonContinuousTable(int tabSize, T[] tab){
		data = tab;
		sizeMax = tabSize;
		size = 0;
		dataCurs = 0;
	}
	
	private T add(T t, int idx){
		if(size < sizeMax){
			if(data[idx] == null){
				data[idx] = t;
				size++;
				if(size < sizeMax){
					if(data[dataCurs] != null){
						boolean update = false;
						while(!update){
							dataCurs++;
					
							if(data[dataCurs] == null)
								update = true;
						}
					}	
				}
				else
					dataCurs = sizeMax;
				
				return null;
			}
			else{
				T tmp = data[idx];
				data[idx] = t;
				return tmp;
			}
		}
		else
			return t;
	}
	
	public T add(T t){
		return add(t, dataCurs);
	}
	
	
	public T remove(int idx){
		if(data[idx] != null){
			T t = data[idx];
			size--;
			
			if(dataCurs >= idx)
				dataCurs = idx;
			
			data[idx] = null;
			return t;
		}
		
		return null;
	}
	
	public void move(int src, int dst){
		if(data[dst] != null){
			T tmp = add(data[src], dst);
			add(tmp, src);
		}
		else{
			T tmp = remove(src);
			add(tmp, dst);
		}
	}
	
	public T access(int idx){
		return data[idx];
	}
}