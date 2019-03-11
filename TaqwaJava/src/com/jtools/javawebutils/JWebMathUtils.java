package com.jtools.javawebutils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Vector;

public class JWebMathUtils {
	// Constants
	public static int SIMPLE = 0;
	public static int EXPONENTIAL = 1;
	/**
	 * Square root of 2<img border=0 alt="pi" src="doc-files/pi.gif">.
	 */
	private final static double SQRT2PI = 2.5066282746310005024157652848110452530069867406099;
	private final static double LOGSQRT2PI = Math.log(SQRT2PI);
	/**
	 * The largest argument for which <code>logGamma(x)</code> is
	 * representable in the machine.
	 */
	public final static double LOG_GAMMA_X_MAX_VALUE = 2.55e305;
	private static final double FINDROOT_ACCURACY = 1.0e-15;
	private static final int FINDROOT_MAX_ITERATIONS = 150;

	// Some IEEE machine constants
	/**
	 * Relative machine precision.
	 */
	private final static double EPS = 2.22e-16;
	private final static int MAX_ITERATIONS = 1000;
	private final static double PRECISION = 4.0 * EPS;
	/**
	 * The smallest positive floating-point number such that 1/xminin is machine
	 * representable.
	 */
	private final static double XMININ = 2.23e-308;

	// Log Gamma related constants
	private final static double lg_d1 = -0.5772156649015328605195174;
	private final static double lg_d2 = 0.4227843350984671393993777;
	private final static double lg_d4 = 1.791759469228055000094023;
	private final static double lg_p1[] = { 4.945235359296727046734888, 201.8112620856775083915565, 2290.838373831346393026739, 11319.67205903380828685045, 28557.24635671635335736389,
			38484.96228443793359990269, 26377.48787624195437963534, 7225.813979700288197698961 };
	private final static double lg_q1[] = { 67.48212550303777196073036, 1113.332393857199323513008, 7738.757056935398733233834, 27639.87074403340708898585, 54993.10206226157329794414,
			61611.22180066002127833352, 36351.27591501940507276287, 8785.536302431013170870835 };
	private final static double lg_p2[] = { 4.974607845568932035012064, 542.4138599891070494101986, 15506.93864978364947665077, 184793.2904445632425417223, 1088204.76946882876749847,
			3338152.967987029735917223, 5106661.678927352456275255, 3074109.054850539556250927 };
	private final static double lg_q2[] = { 183.0328399370592604055942, 7765.049321445005871323047, 133190.3827966074194402448, 1136705.821321969608938755, 5267964.117437946917577538,
			13467014.54311101692290052, 17827365.30353274213975932, 9533095.591844353613395747 };
	private final static double lg_p4[] = { 14745.02166059939948905062, 2426813.369486704502836312, 121475557.4045093227939592, 2663432449.630976949898078, 29403789566.34553899906876,
			170266573776.5398868392998, 492612579337.743088758812, 560625185622.3951465078242 };
	private final static double lg_q4[] = { 2690.530175870899333379843, 639388.5654300092398984238, 41355999.30241388052042842, 1120872109.61614794137657, 14886137286.78813811542398,
			101680358627.2438228077304, 341747634550.7377132798597, 446315818741.9713286462081 };
	private final static double lg_c[] = { -0.001910444077728, 8.4171387781295e-4, -5.952379913043012e-4, 7.93650793500350248e-4, -0.002777777777777681622553, 0.08333333333333333331554247,
			0.0057083835261 };
	// Rough estimate of the fourth root of logGamma_xBig
	private final static double lg_frtbig = 2.25e76;
	private final static double pnt68 = 0.6796875;

	/**
	 * Check if the range of the argument of the distribution method is between
	 * <code>lo</code> and <code>hi</code>.
	 * 
	 * @exception IllegalArgumentException
	 *                If the argument is out of range.
	 */
	private static void checkRange(double x, double lo, double hi) {
		if (x < lo || x > hi)
			throw new IllegalArgumentException("The argument of the distribution method should be between " + lo + " and " + hi + ".");
	}

	/**
	 * Check if the range of the argument of the distribution method is between
	 * 0.0 and 1.0.
	 * 
	 * @exception IllegalArgumentException
	 *                If the argument is out of range.
	 * 
	 */
	private static void checkRange(double x) {
		if (x < 0.0 || x > 1.0)
			throw new IllegalArgumentException("The argument of the distribution method should be between 0.0 and 1.0.");
	}

	public static double aveDev(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double mean = mean(v);
		double aveDev = 0;

		for (int i = 0; i < v.length; i++) {
			double temp = v[i] - mean;
			if (temp < 0) {
				temp = -temp;
			}
			aveDev += temp;
		}

		aveDev = aveDev / v.length;

		return aveDev;
	}

	public static double average(double[] list) {
		double avrg = 0.0f;
		System.err.println("list.length = " + list.length);
		double[] x = getNonZero(list);
		System.err.println("x.length = " + x.length);
		if (x != null) {
			for (int i = 0; i < x.length; i++) {
				System.err.println("x[" + i + "] = " + x[i]);
				avrg += x[i];
			}
			System.err.println("sum = " + avrg);
			avrg = avrg / x.length;
		}
		return avrg;
	}

	/*
	 * public static double average(double[] x){ double avrg=0.0f;
	 * 
	 * System.err.println("ENTER AVG");
	 * 
	 * 
	 * if(x!=null){ for(int i=0;i<x.length;i++){ avrg+=x[i]; }
	 * avrg=avrg/x.length; } System.err.println("EXIT AVG"); return avrg; }
	 */

	/**
	 * The natural logarithm of the gamma function. Based on public domain
	 * NETLIB (Fortran) code by W. J. Cody and L. Stoltz<BR>
	 * Applied Mathematics Division<BR>
	 * Argonne National Laboratory<BR>
	 * Argonne, IL 60439<BR>
	 * <P>
	 * References:
	 * <OL>
	 * <LI>W. J. Cody and K. E. Hillstrom, 'Chebyshev Approximations for the
	 * Natural Logarithm of the Gamma Function,' Math. Comp. 21, 1967, pp.
	 * 198-203.
	 * <LI>K. E. Hillstrom, ANL/AMD Program ANLC366S, DGAMMA/DLGAMA, May, 1969.
	 * <LI>Hart, Et. Al., Computer Approximations, Wiley and sons, New York,
	 * 1968.
	 * </OL>
	 * </P>
	 * <P>
	 * From the original documentation:
	 * </P>
	 * <P>
	 * This routine calculates the LOG(GAMMA) function for a positive real
	 * argument X. Computation is based on an algorithm outlined in references 1
	 * and 2. The program uses rational functions that theoretically approximate
	 * LOG(GAMMA) to at least 18 significant decimal digits. The approximation
	 * for X > 12 is from reference 3, while approximations for X < 12.0 are
	 * similar to those in reference 1, but are unpublished. The accuracy
	 * achieved depends on the arithmetic system, the compiler, the intrinsic
	 * functions, and proper selection of the machine-dependent constants.
	 * </P>
	 * <P>
	 * Error returns:<BR>
	 * The program returns the value XINF for X .LE. 0.0 or when overflow would
	 * occur. The computation is believed to be free of underflow and overflow.
	 * </P>
	 * 
	 * @return Double.MAX_VALUE for x < 0.0 or when overflow would occur, i.e. x >
	 *         2.55E305
	 * @author Jaco van Kooten
	 */
	private static double logGamma(double x) {
		double xden, corr, xnum;
		int i;
		double y, xm1, xm2, xm4, res, ysq;
		y = x;
		if (y > 0.0 && y <= 2.55e305) {
			// Relative machine precision.
			if (y <= 2.22e-16) {
				res = -Math.log(y);
			} else if (y <= 1.5) {
				// ----------------------------------------------------------------------
				// EPS .LT. X .LE. 1.5
				// ----------------------------------------------------------------------
				// Rough estimate of the fourth root of logGamma_xBig
				if (y < pnt68) {
					corr = -Math.log(y);
					xm1 = y;
				} else {
					corr = 0.0;
					xm1 = y - 1.0;
				}
				if (y <= 0.5 || y >= pnt68) {
					xden = 1.0;
					xnum = 0.0;
					for (i = 0; i < 8; i++) {
						xnum = xnum * xm1 + lg_p1[i];
						xden = xden * xm1 + lg_q1[i];
					}

					res = corr + xm1 * (lg_d1 + xm1 * (xnum / xden));
				} else {
					xm2 = y - 1.0;
					xden = 1.0;
					xnum = 0.0;

					for (i = 0; i < 8; i++) {
						xnum = xnum * xm2 + lg_p2[i];
						xden = xden * xm2 + lg_q2[i];
					}

					res = corr + xm2 * (lg_d2 + xm2 * (xnum / xden));
				}
			} else if (y <= 4.0) {
				// ----------------------------------------------------------------------
				// 1.5 .LT. X .LE. 4.0
				// ----------------------------------------------------------------------
				xm2 = y - 2.0;
				xden = 1.0;
				xnum = 0.0;

				for (i = 0; i < 8; i++) {
					xnum = xnum * xm2 + lg_p2[i];
					xden = xden * xm2 + lg_q2[i];
				}
				res = xm2 * (lg_d2 + xm2 * (xnum / xden));

			} else if (y <= 12.0) {
				// ----------------------------------------------------------------------
				// 4.0 .LT. X .LE. 12.0
				// ----------------------------------------------------------------------
				xm4 = y - 4.0;
				xden = -1.0;
				xnum = 0.0;
				for (i = 0; i < 8; i++) {
					xnum = xnum * xm4 + lg_p4[i];
					xden = xden * xm4 + lg_q4[i];
				}
				res = lg_d4 + xm4 * (xnum / xden);
			} else {
				// ----------------------------------------------------------------------
				// Evaluate for argument .GE. 12.0
				// ----------------------------------------------------------------------
				res = 0.0;
				if (y <= lg_frtbig) {
					res = lg_c[6];
					ysq = y * y;
					for (i = 0; i < 6; i++)
						res = res / ysq + lg_c[i];
				}
				res /= y;
				corr = Math.log(y);
				res = res + LOGSQRT2PI - 0.5 * corr;
				res += y * (corr - 1.0);
			}
		} else {
			// ----------------------------------------------------------------------
			// Return for bad arguments
			// ----------------------------------------------------------------------
			res = Double.MAX_VALUE;
		}
		// ----------------------------------------------------------------------
		// Final adjustments and return
		// ----------------------------------------------------------------------
		return res;
	}

	public static double betaDist(double x, double p, double q) {
		if (x < 0.0 || x > 1.0)
			throw new IllegalArgumentException("The argument of the distribution method should be between 0.0 and 1.0.");

		if (x <= 0.0)
			return 0.0;
		else if (x >= 1.0)
			return 1.0;
		else if (p <= 0.0 || q <= 0.0 || (p + q) > 2.55e305)
			return 0.0;
		else {
			double logBeta = logGamma(p) + logGamma(q) - logGamma(p + q);
			double beta_gam = Math.exp(-logBeta + p * Math.log(x) + q * Math.log(1.0 - x));
			if (x < (p + 1.0) / (p + q + 2.0))
				return beta_gam * betaFraction(x, p, q) / p;
			else
				return 1.0 - (beta_gam * betaFraction(1.0 - x, q, p) / q);
		}
	}

	/**
	 * Evaluates of continued fraction part of incomplete beta function. Based
	 * on an idea from Numerical Recipes (W.H. Press et al, 1992).
	 * 
	 * @author Jaco van Kooten
	 */
	private static double betaFraction(double x, double p, double q) {
		int m, m2;
		double sum_pq, p_plus, p_minus, c = 1.0, d, delta, h, frac;
		sum_pq = p + q;
		p_plus = p + 1.0;
		p_minus = p - 1.0;
		h = 1.0 - sum_pq * x / p_plus;
		if (Math.abs(h) < XMININ)
			h = XMININ;

		h = 1.0 / h;
		frac = h;
		m = 1;
		delta = 0.0;

		while (m <= MAX_ITERATIONS && Math.abs(delta - 1.0) > PRECISION) {
			m2 = 2 * m;
			// even index for d
			d = m * (q - m) * x / ((p_minus + m2) * (p + m2));
			h = 1.0 + d * h;

			if (Math.abs(h) < XMININ)
				h = XMININ;

			h = 1.0 / h;
			c = 1.0 + d / c;
			if (Math.abs(c) < XMININ)
				c = XMININ;

			frac *= h * c;
			// odd index for d
			d = -(p + m) * (sum_pq + m) * x / ((p + m2) * (p_plus + m2));
			h = 1.0 + d * h;
			if (Math.abs(h) < XMININ)
				h = XMININ;

			h = 1.0 / h;
			c = 1.0 + d / c;

			if (Math.abs(c) < XMININ)
				c = XMININ;

			delta = h * c;
			frac *= delta;
			m++;
		}
		return frac;
	}

	/**
	 * This method approximates the value of X for which P(x&lt;X)=<I>prob</I>.
	 * It applies a combination of a Newton-Raphson procedure and bisection
	 * method with the value <I>guess</I> as a starting point. Furthermore, to
	 * ensure convergency and stability, one should supply an inverval [<I>xLo</I>,<I>xHi</I>]
	 * in which the probalility distribution reaches the value <I>prob</I>. The
	 * method does no checking, it will produce bad results if wrong values for
	 * the parameters are supplied - use it with care.
	 */
	private static double findRoot(double prob, double guess, double xLo, double xHi, double p, double q) {
		double x = guess, xNew = guess;
		double error, pdf, dx = 1.0;
		int i = 0;
		while (Math.abs(dx) > FINDROOT_ACCURACY && i++ < FINDROOT_MAX_ITERATIONS) {
			// Apply Newton-Raphson step
			error = betaDist(x, p, q) - prob;
			if (error < 0.0)
				xLo = x;
			else
				xHi = x;
			pdf = betaProbability(x, p, q);
			if (pdf != 0.0) {
				// Avoid division by zero
				dx = error / pdf;
				xNew = x - dx;
			}
			// If the Newton-Raphson fails to converge (which for example may be
			// the
			// case if the initial guess is to rough) we apply a bisection
			// step to determine a more narrow interval around the root.
			if (xNew < xLo || xNew > xHi || pdf == 0.0) {
				xNew = (xLo + xHi) / 2.0;
				dx = xNew - x;
			}
			x = xNew;
		}
		return x;
	}

	/**
	 * The natural logarithm of the beta function.
	 * 
	 * @param p
	 *            require p>0
	 * @param q
	 *            require q>0
	 * @return 0 if p<=0, q<=0 or p+q>2.55E305 to avoid errors and
	 *         over/underflow
	 * @author Jaco van Kooten
	 */
	public static double logBeta(double p, double q) {
		double val = 0;
		if (p <= 0.0 || q <= 0.0 || (p + q) > LOG_GAMMA_X_MAX_VALUE)
			val = 0.0;
		else
			val = logGamma(p) + logGamma(q) - logGamma(p + q);

		return val;
	}

	private static double betaProbability(double X, double p, double q) {
		checkRange(X);
		if (X == 0.0 || X == 1.0)
			return 0.0;
		return Math.exp(-logBeta(p, q) + (p - 1.0) * Math.log(X) + (q - 1.0) * Math.log(1.0 - X));
	}

	public static double betaInv(double probability, double p, double q) {
		checkRange(probability);
		if (probability == 0.0)
			return 0.0;
		if (probability == 1.0)
			return 1.0;
		return findRoot(probability, 0.5, 0.0, 1.0, p, q);
	}

	/**
	 * Probability density function of a binomial distribution.
	 * 
	 * @param X
	 *            should be integer-valued.
	 * @return the probability that a stochastic variable x has the value X,
	 *         i.e. P(x=X).
	 */
	private static double binomProb(double X, int n, double p) {
		checkRange(X, 0.0, n);
		return binomial(n, X) * Math.pow(p, X) * Math.pow(1.0 - p, n - X);
	}

	public static double binomial(double n, double k) {
		return Math.exp(logGamma(n + 1.0) - logGamma(k + 1.0) - logGamma(n - k + 1.0));
	}

	/**
	 * Cumulative binomial distribution function.
	 * 
	 * @param X
	 *            should be integer-valued.
	 * @return the probability that a stochastic variable x is less then X, i.e.
	 *         P(x&lt;X).
	 */
	public static double BINOMDIST(double X, int trials, double prob) {
		checkRange(X, 0.0, trials);
		double sum = 0.0;
		for (double i = 0.0; i <= X; i++)
			sum += binomProb(i, trials, prob);
		return sum;
	}

	private static double mass(double[] v) {
		double somme = 0.0;
		for (int k = 0; k < v.length; k++) {
			somme += v[k];
		}
		return (somme);
	}

	private static double mean(double[] v) {
		if (v.length == 0)
			throw new IllegalArgumentException("Nothing to compute! The array must have at least one element.");
		return (mass(v) / (double) v.length);
	}

	private static double variance(double[] v) {
		final double m = mean(v);
		double ans = 0.0;
		for (int i = 0; i < v.length; i++)
			ans += (v[i] - m) * (v[i] - m);
		return ans / (v.length - 1);
	}

	/**
	 * Computes the covariance.
	 */
	private static double covariance(double[] v1, double[] v2) {
		if (v1.length != v2.length)
			throw new IllegalArgumentException("Arrays must have the same length : " + v1.length + ", " + v2.length);
		final double m1 = mean(v1);
		final double m2 = mean(v2);
		double ans = 0.0;
		for (int i = 0; i < v1.length; i++)
			ans += (v1[i] - m1) * (v2[i] - m2);
		return ans / (v1.length - 1);
	}

	public static double corRel(double[] v1, double[] v2) {
		double denom = Math.sqrt(variance(v1) * variance(v2));
		if (denom != 0)
			return (covariance(v1, v2) / denom);
		else {
			if ((variance(v1) == 0) && (variance(v2) == 0))
				return (1.0);
			else
				return (0.0); // impossible to correlate a null signal with
			// another
		}
	}

	public static double coVar(double[] v1, double[] v2) {
		if (v1.length != v2.length)
			throw new IllegalArgumentException("Arrays must have the same length : " + v1.length + ", " + v2.length);

		double m1 = mean(v1);
		double m2 = mean(v2);

		double ans = 0.0;
		for (int i = 0; i < v1.length; i++)
			ans += (v1[i] - m1) * (v2[i] - m2);
		return ans / (v1.length - 1);
	}

	public static double devSq(double[] v) {
		double m = mean(v);
		double ans = 0;
		for (int i = 0; i < v.length; i++)
			ans += Math.pow((v[i] - m), 2);
		return ans / (v.length - 1);
	}

	public static double exponDist(double X, double lambda) {
		checkRange(X, 0.0, Double.MAX_VALUE);
		return 1.0 - Math.exp(-lambda * X);
	}

	public static double fDist(double X, double p, double q) {
		checkRange(X, 0.0, Double.MAX_VALUE);
		return betaDist((p * X) / (q + (p * X)), p / 2.0, q / 2.0);
	}

	public static double fInv(double probability, double p, double q) {
		checkRange(probability);
		if (probability == 0.0)
			return 0.0;
		if (probability == 1.0)
			return Double.MAX_VALUE;
		double y = betaInv(probability, p, q);
		if (y < 2.23e-308) // avoid overflow
			return Double.MAX_VALUE;
		else
			return (q / p) * (y / (1.0 - y));
	}

	/**
	 * F=a+bx where a=y`-b*x` and b= sum[(x-x`)*(y-y`)]/sum[square(x-x`)]
	 * 
	 * @param x
	 * @param v1
	 * @param v2
	 */
	public static double forecast(double x, double[] v1, double[] v2) {
		if (v1 == null || v2 == null)
			throw new NullPointerException();

		if (v1.length != v2.length)
			throw new IllegalArgumentException("Arrays must have the same length : " + v1.length + ", " + v2.length);

		double mean_v1 = mean(v1);
		double mean_v2 = mean(v2);

		// calulating b
		double nominator = 0, denominator = 0;

		for (int i = 0; i < v1.length; i++) {
			nominator += (v1[i] - mean_v1) * (v2[i] - mean_v2);
			denominator += (v1[i] - mean_v1) * (v1[i] - mean_v1);
		}

		double b = nominator / denominator;
		double a = mean_v2 - b * mean_v1;

		return (a + b * x);
	}

	public static double geoMean(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double ans = 0;

		for (int i = 0; i < v.length; i++) {
			ans *= v[i];
		}

		ans = Math.pow(ans, (1.0 / (double) v.length));
		return ans;
	}

	/**
	 * 
	 * 1/H = 1/n*SumUptoN(1/x)
	 * 
	 * @param v
	 * @return
	 */
	public static double harMean(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double inverse = 0;

		for (int i = 0; i < v.length; i++) {
			inverse += (1.0 / v[i]);
		}

		inverse *= (1.0 / (double) v.length);

		return (1.0 / inverse);

	}

	/**
	 * http://office.microsoft.com/en-ca/assistance/HP052091171033.aspx
	 * 
	 * @param sample_suc
	 * @param sampleSize
	 * @param population_suc
	 * @param populationSize
	 * @return
	 */
	public static double hypGeomDist(int x, int n, int M, int N) {
		if (x > n || M > N)
			throw new IllegalArgumentException("x should be less than n and M less than N");
		return factorial(M) * factorial(N - M) * factorial(n) * factorial(N - n) / (factorial(x - M) * factorial(x) * factorial(n - x) * factorial(N - M - n + x) * factorial(N));
	}

	public static double intercept(double[] y, double[] x) {
		if (x == null || y == null)
			throw new NullPointerException();

		if (x.length != y.length)
			throw new IllegalArgumentException("size of the two arrays must be same");

		double mean_x = mean(x);
		double mean_y = mean(y);

		double denominator = 0, nominator = 0;

		for (int i = 0; i < x.length; i++) {
			nominator += (x[i] - mean_x) * (y[i] - mean_y);
			denominator += (x[i] - mean_x) * (x[i] - mean_x);
		}

		double b = nominator / denominator;

		return mean_y - b * mean_x;

	}

	/**
	 * http://office.microsoft.com/en-ca/assistance/HP052091501033.aspx
	 * 
	 * @param v
	 * @return
	 */

	public static double kurt(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double mean = mean(v);

		double s = stDevP(v);
		double sum = 0.0;
		double n = (double) v.length;

		for (int i = 0; i < v.length; i++) {
			sum += (v[i] - mean) / s;
		}

		return n * (n + 1) * sum / ((n - 1) * (n - 2) * (n - 3)) - 3 * (n - 1) * (n - 1) / ((n - 2) * (n - 3));
	}

	public static double large(double[] v, int k) {
		if (v == null)
			throw new NullPointerException();

		if (k > v.length)
			throw new IllegalArgumentException("k should not be greater than the array's lenght");

		double[] temp = v;

		Arrays.sort(temp);

		return temp[k];

	}

	public static double max(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double temp = v[0];

		for (int i = 0; i < v.length; i++) {
			if (v[i] > temp) {
				temp = v[i];
			}
		}

		return temp;

	}

	/*
	 * public static double median(double[] d) { if(d==null) { throw new
	 * NullPointerException(); } double[] v= d; Arrays.sort(v); double
	 * median=v[0]; if(v.length!=1) { if(v.length%2==0) { median =
	 * (v[v.length/2]+v[(v.length/2)+1])/2; } else{ median =
	 * v[((v.length-1)/2)+1]; } } return median; }
	 */

	public static double median(double[] d) {
		if (d == null) {
			throw new NullPointerException();
		}
		double[] v = d;
		Arrays.sort(v);
		double median = v[0];

		if (v.length != 1) {
			if (v.length % 2 == 0) {
				median = (v[(v.length - 1) / 2] + v[(v.length / 2)]) / 2;
			} else {
				median = v[((v.length - 1) / 2)];
			}
		}
		return median;
	}

	public static double min(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double temp = v[0];

		for (int i = 0; i < v.length; i++) {
			if (v[i] < temp) {
				temp = v[i];
			}
		}

		return temp;
	}

	/*
	 * public static double mode(double[] v){ if(v==null) throw new
	 * NullPointerException();
	 * 
	 * double[] temp=v;
	 * 
	 * Arrays.sort(temp);
	 * 
	 * int count=0,index=0,maxcount=0;
	 * 
	 * for(int i=0;i<temp.length;i++){ count=0; for(int j=i;j<temp.length;j++){
	 * if(temp[i]!=temp[j]){ if(count>maxcount){ index=i; maxcount=count; }
	 * i+=count; break; } else{ count++; } } } return temp[index]; }
	 */

	public static double mode(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double[] temp = v;

		Arrays.sort(temp);

		int count = 0, index = 0, maxcount = 0;

		for (int i = 0; i < temp.length; i++) {
			count = 0;
			for (int j = i; j < temp.length; j++) {
				if (temp[i] != temp[j]) {
					i += (count - 1);
					break;
				} else {
					count++;
				}
				if (count > maxcount) {
					index = i;
					maxcount = count;
				}
			}
		}
		return temp[index];
	}

	public static double percentile(double[] v, double p) {
		if (v == null)
			throw new NullPointerException();

		if ((p < 0) || (p > 1)) {
			throw new IllegalArgumentException("Percentile must be between 0 and 1 : " + p);
		}
		final double[] ans = v;
		Arrays.sort(ans);
		final int pos = (int) Math.floor(p * (ans.length - 1));
		final double dif = p * (ans.length - 1) - Math.floor(p * (ans.length - 1));
		if (pos == (ans.length - 1))
			return (ans[ans.length - 1]);
		else
			return (ans[pos] * (1.0 - dif) + ans[pos + 1] * dif);
	}

	/*
	 * public static double percentile(double[] v, double p) { if(v==null) throw
	 * new NullPointerException(); // if(p<0 || p>1) // throw new
	 * IllegalArgumentException("p should be between 0 & 1");
	 * 
	 * double[] temp = v;
	 * 
	 * if(v.length==1) return v[0];
	 * 
	 * Arrays.sort(temp);
	 * 
	 * double d = p*(temp.length+1); int pos = (int)d; d=d-pos;
	 * 
	 * return temp[pos] + d * (temp[pos+1] - temp[pos]); }
	 */

	/**
	 * http://www.psychstat.missouristate.edu/introbook2/sbk12.xml
	 * 
	 * PR = (Fb+Fx/2)*100/n
	 * 
	 * @param v
	 * @param x
	 * @return
	 */
	public static double percentRank(double[] v, double x) {
		if (v == null)
			throw new NullPointerException();

		int freq_bellow = 0, freq_x = 0;

		double[] temp = v;

		Arrays.sort(temp);

		for (int i = 0; i < temp.length; i++) {
			if (temp[i] < x) {
				freq_bellow++;
			} else if (temp[i] == x) {
				freq_x++;
			} else {
				break;
			}
		}
		return (freq_bellow + 0.5 * freq_x) * 100 / temp.length;
	}

	private static int factorial(int x) {
		if (x == 0 || x == 1)
			return x;

		int val = 1;

		for (int i = 0; i < x; i++) {
			val *= (i + 1);
		}
		return val;
	}

	/**
	 * P=n!/(n-k)!
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static int permut(int n, int k) {
		if (n < k)
			throw new IllegalArgumentException("k should be lesser than n");

		return factorial(n) / factorial(n - k);

	}

	public static double prob(double[] v, double[] p, double lower) {
		return prob(v, p, lower, lower);
	}

	public static double prob(double[] v, double[] p, double lower, double upper) {
		if (v == null || p == null)
			throw new NullPointerException();

		if (v.length != p.length)
			throw new IllegalArgumentException("both arrays should have equal length");

		double total = 0;
		for (int i = 0; i < p.length; i++) {
			total += p[i];
		}

		if (total != 1.0)
			throw new IllegalArgumentException("Sum of all probabilities should be 1.0");

		double prob = 0;

		for (int i = 0; i < v.length; i++) {
			if (v[i] > lower && v[i] < upper) {
				prob += p[i];
			}
		}

		return prob;
	}

	/**
	 * http://support.microsoft.com/?kbid=214072
	 * 
	 * @param v
	 * @param q
	 */
	public static double quartile(double[] v, int q) {
		if (v == null)
			throw new NullPointerException();

		if (v.length == 1)
			return v[0];

		double[] temp = v;

		Arrays.sort(temp);
		if (q > 3)
			return temp[temp.length - 1];

		int n = temp.length;
		double f = ((double) q / 4) * (n - 1) + 1;

		int k = (int) f;

		f = f - k;
		k--;// kth smallest element not the kth index is required

		// System.out.println("f="+f+", k="+k);
		//		
		// System.out.println("temp["+k+"]+("+f+"*(temp["+(k+1)+"]-temp["+k+"]");
		// System.out.println(temp[k]+"+("+f+"*("+temp[k+1]+"-"+temp[k]+")");

		return temp[k] + (f * (temp[k + 1] - temp[k]));

	}

	/**
	 * 
	 * @param num
	 * @param v
	 * @return index of the value after sorting the array, 0 if not in the
	 *         array.
	 */
	public static int rank(double num, double[] v) {
		if (v == null)
			throw new NullPointerException();

		double[] temp = v;

		Arrays.sort(temp);
		int val = 0;

		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == num) {
				val = i;
				break;
			}
		}
		return val;
	}

	/**
	 * r=sum[(x-x`)*(y-y`)]/sqrt{sum[x-x`]*sum[y-y`]} RSQ=r*r
	 * 
	 * @param v1
	 * @param v2
	 */
	public static double rsq(double[] v1, double[] v2) {
		System.err.println("v1.length=" + v1.length + ", v2.length=" + v2.length);
		if (v1 == null || v2 == null)
			throw new NullPointerException();

		if (v1.length != v2.length) {
			System.err.println("Not equal");
			throw new IllegalArgumentException("both arrays should have equal length");
		}

		System.err.println("taking mean");

		double mean_v1 = mean(v1);
		double mean_v2 = mean(v2);

		System.err.println("\tmean_v1=" + mean_v1 + "\tmean_v2=" + mean_v2);

		double nominator = 0, v1Dev = 0, v2Dev = 0;

		for (int i = 0; i < v1.length; i++) {
			nominator += ((v1[i] - mean_v1) * (v2[i] - mean_v2));

			v1Dev += Math.pow((v1[i] - mean_v1), 2);
			v2Dev += Math.pow((v2[i] - mean_v2), 2);
		}

		double r = nominator / Math.sqrt(v1Dev * v2Dev);

		System.err.println("\trsq return = " + (r * r));

		return r * r;
	}

	/**
	 * skewness = [n/(n-1)(n-2)]*pow3(sum(x-x`)/std)
	 * 
	 * @param v
	 * @return
	 */
	public static double skew(double[] v) {
		if (v == null)
			throw new NullPointerException();
		if (v.length < 3)
			throw new IllegalArgumentException("use more than 3 values");
		double standDev = stDev(v);
		if (standDev == 0)
			throw new IllegalArgumentException("standard deviation is zero");
		double mean = mean(v);
		int n = v.length;

		double skew = 0;

		for (int i = 0; i < v.length; i++) {
			skew += Math.pow(((v[i] - mean) / standDev), 3);
		}

		skew = skew * n / ((n - 1) * (n - 2));

		return skew;
	}

	/**
	 * val = sum((x-x`)(y-y`))/sum((x-x`)pow2)
	 * 
	 * @param x
	 * @param y
	 */
	public static double slope(double[] x, double[] y) {
		if (x == null || y == null)
			throw new NullPointerException();

		if (x.length != y.length)
			throw new IllegalArgumentException("both arrays should have equal length");

		double mean_x = mean(x);
		double mean_y = mean(y);

		double nominator = 0, denominator = 0;

		for (int i = 0; i < x.length; i++) {
			nominator += ((x[i] - mean_x) * (y[i] - mean_y));
		}

		for (int i = 0; i < x.length; i++) {
			denominator += Math.pow((x[i] - mean_x), 2);
		}

		return nominator / denominator;
	}

	public static double small(double[] v, int k) {
		if (k > v.length)
			throw new IllegalArgumentException();

		double[] temp = v;
		Arrays.sort(temp);
		return temp[k - 1];
	}

	/**
	 * Z=(x-mean)/SD
	 * 
	 * @param x
	 * @param mean
	 * @param SD
	 */

	public static double standardize(double x, double mean, double SD) {
		return (x - mean) / SD;
	}

	public static double stDev(double[] v) {
		if (v == null)
			throw new NullPointerException();

		return Math.sqrt(var(v));
	}

	public static double stDevP(double[] v) {
		if (v == null)
			throw new NullPointerException();

		return Math.sqrt(varP(v));
	}

	public static double trimMean(double[] v, double percentage) {
		if (v == null)
			throw new NullPointerException();
		if (percentage < 0 || percentage > 1)
			throw new IllegalArgumentException("percentage should be between 0 and 1");

		int range = (int) ((percentage * v.length) / 2);

		double mean = 0;
		if ((range * 2) < v.length) {
			for (int i = range; i < (v.length - range); i++) {
				mean += v[i];
			}

			mean = mean / (v.length - range * 2);
		}
		return mean;

	}

	/***************************************************************************
	 * sample variance = [sum(x-mean)]/(n-1)
	 */
	public static double var(double[] v) {
		if (v == null || v.length < 2)
			throw new NullPointerException();

		double mean = mean(v);
		double variance = 0;

		for (int i = 0; i < v.length; i++) {
			variance += Math.pow((v[i] - mean), 2);
		}

		variance = variance / (v.length - 1);

		return variance;
	}

	/***************************************************************************
	 * entire population variance = [sum(x-mean)]/(n)
	 */
	public static double varP(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double mean = mean(v);
		double variance = 0;

		for (int i = 0; i < v.length; i++) {
			variance += Math.pow((v[i] - mean), 2);
		}

		variance = variance / v.length;
		return variance;
	}

	public static double[] movingAverage(double[] v, int period, int flag) {
		if (v == null)
			throw new NullPointerException();

		if (period > v.length)
			throw new IllegalArgumentException("Period cannot exceed the data size");

		double[] result = new double[v.length];
		double temp = 0.0;
		double periodMultiplier = 2.0 / (1.0 + period);
		System.out.println("mult = " + periodMultiplier);

		for (int i = 0; i < v.length; i++) {
			temp += v[i];
			if ((i + 1) < period)
				result[i] = 0;
			else {
				if (flag == SIMPLE) {
					if ((i + 1) > period) {
						temp -= v[i - period];
					}

					result[i] = temp / period;
				} else {
					if ((i + 1) == period)
						result[i] = temp / period;
					else {
						result[i] = (v[i] - result[i - 1]) * periodMultiplier + result[i - 1];
					}
				}
			}
		}

		return result;
	}

	public static double[] rsi(double[] v, int period) {
		if (v == null)
			throw new NullPointerException();

		if (period > v.length)
			throw new IllegalArgumentException("Period cannot exceed the data size");

		double[] result = new double[v.length];

		double avrgGain = 0.0, avrgLoss = 0.0;

		for (int i = 0; i < v.length; i++) {
			if (i > 0) {
				if ((i) < period) {
					if ((v[i] - v[i - 1]) > 0)
						avrgGain += (v[i] - v[i - 1]);
					else
						avrgLoss -= (v[i] - v[i - 1]);
				} else if ((i) == period) {
					if ((v[i] - v[i - 1]) > 0)
						avrgGain += (v[i] - v[i - 1]);
					else
						avrgLoss -= (v[i] - v[i - 1]);

					avrgGain = avrgGain / period;
					avrgLoss = avrgLoss / period;
				} else {
					if ((v[i] - v[i - 1]) > 0) {
						avrgGain = (avrgGain * (period - 1) + (v[i] - v[i - 1])) / period;
						avrgLoss = (avrgLoss * (period - 1) + 0) / period;
					} else {
						avrgLoss = (avrgLoss * (period - 1) - (v[i] - v[i - 1])) / period;
						avrgGain = (avrgGain * (period - 1) + 0) / period;
					}
				}
			}
			if ((i) < period)
				result[i] = 0;
			else {
				double RS = 0.0;
				if (avrgLoss != 0)
					RS = avrgGain / avrgLoss;
				else
					RS = 1;
				result[i] = 100 - 100 / (1 + RS);
			}
		}

		return result;
	}

	/**
	 * stochasticOscillator
	 * 
	 * @param high
	 * @param low
	 * @param close
	 * @return double stochastic value
	 */
	public static double[] stochasticOscillator(double[] high, double[] low, double[] close, int period, int slowing) {
		System.out.println("high=" + high.length + "\nlow=" + low.length + "\nclose=" + close.length);
		if (high == null || low == null || close == null)
			throw new NullPointerException();

		if (high.length != low.length || high.length != close.length)
			throw new IllegalArgumentException("High, Low and Close arrays should be of equal lenghts");

		if (period > high.length)
			throw new IllegalArgumentException("Period cannot exceed the data size");

		double[] result = new double[high.length];

		int lowestIndex = 0, secondLowestIndex = -1, highestIndex = 0, secondHighestIndex = -1;

		for (int i = 1; i < high.length; i++) {
			// Calculating highest high within the interval
			if (highestIndex <= (i - period)) {
				highestIndex = i - period + 1;
				for (int j = (i - period + 1); j < i; j++) {
					if (high[j] > high[highestIndex]) {
						highestIndex = j;
					}
				}
			}

			if (high[i] > high[highestIndex]) {
				highestIndex = i;
			}

			// Calculating lowest low within the interval
			if (lowestIndex <= (i - period)) {
				lowestIndex = i - period + 1;
				for (int j = (i - period + 1); j < i; j++) {
					if (low[j] < low[lowestIndex]) {
						lowestIndex = j;
					}
				}
			}
			if (low[i] < low[lowestIndex]) {
				lowestIndex = i;
			}

			if (i < (period - 1)) {
				result[i] = 0;
			} else {
				result[i] = 100 * (close[i] - low[lowestIndex]) / (high[highestIndex] - low[lowestIndex]);
			}
		}

		return result;
	}

	public static double[] getNonZero(double[] d) {
		double[] temp0 = d;
		Arrays.sort(temp0);
		int n = 0;
		for (int i = temp0.length - 1; i >= 0; i--) {
			if (temp0[i] == 0) {
				break;
			}
			n = i;
		}
		double temp[] = new double[temp0.length - n];
		for (int i = n; i < temp0.length; i++) {
			temp[i - n] = temp0[i];
		}
		return temp;
	}

	public static double sum(double[] v) {
		if (v == null)
			throw new NullPointerException();

		double d = 0;

		for (int i = 0; i < v.length; i++) {
			d += v[i];
		}
		return d;
	}

	public static double OpeningPrice(double[] v) {
		// System.err.println(JWebUtils.getArrayAsString(v,","));
		// System.err.println("--------------");
		if (v.length > 0) {
			return v[0];
		} else {
			return 0;
		}
	}

	public static double ClosingPrice(double[] v) {
		// System.err.println(JWebUtils.getArrayAsString(v,","));
		// System.err.println("--------------");
		// Bug - last value is zero why?
		if (v.length > 1) {
			// System.err.println("Last Value:
			// "+v[v.length-1]+","+v[v.length-2]);
		}
		if (v.length > 1) {
			return v[v.length - 1];
		} else {
			return 0;
		}
	}

}
