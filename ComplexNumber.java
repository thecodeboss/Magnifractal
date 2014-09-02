public class ComplexNumber {
	double Re;
	double Im;
	public ComplexNumber(double a, double b) {
		Re = a;
		Im = b;
	}

	public ComplexNumber Add(ComplexNumber B) {
		return new ComplexNumber(Re + B.Re, Im + B.Im);
	}

	public ComplexNumber Subtract(ComplexNumber B) {
		return new ComplexNumber(Re - B.Re, Im - B.Im);
	}

	public ComplexNumber Multiply(ComplexNumber B) {
		return new ComplexNumber(Re*B.Re - Im*B.Im, Re*B.Im + Im*B.Re);
	}

	public ComplexNumber Divide(ComplexNumber B) {
		double denom = B.Re*B.Re + B.Im*B.Im;
		return new ComplexNumber((Re*B.Re + Im*B.Im)/denom, (Im*B.Re - Re*B.Im)/denom);
	}

	public double Mag2() {
		return Re*Re + Im*Im;
	}
}
