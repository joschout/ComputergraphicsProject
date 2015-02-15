package math;

public class MatrixMultiplyTest {

	public static void main(String[] args) {
		Matrix m1 = new Matrix();
		for(int i = 0; i <= 3 ;i++){
			m1.set(0, i, 1);
		}
		System.out.println("=== matrix m1 ===");
		System.out.println(m1.toString());
		
		Matrix m2 = new Matrix();
		for(int i = 0; i <= 3 ;i++){
			m2.set(i, 0, 1);
		}
		System.out.println("=== matrix m2 ===");
		System.out.println(m2.toString());
		
		Matrix m3 = m1.multiply(m2);
		System.out.println("=== m1 times m2 ===");
		System.out.println(m3.toString());
	}

}
