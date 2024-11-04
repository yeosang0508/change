package com.project.tailsroute.util;

public class HelmertTransformation {

	// WGS84 타원체 상수
	private static final double a = 6378137.0; // 장반경 (meters)
	private static final double f = 1 / 298.257223563; // 편평률
	private static final double e2 = 2 * f - f * f; // 이심률 제곱

	// 7-매개변수: 평행 이동, 회전, 축척 변화
	private double tX = 582; // X 방향 평행 이동 (meters)
	private double tY = 105; // Y 방향 평행 이동 (meters)
	private double tZ = 414; // Z 방향 평행 이동 (meters)
	private double rX = Math.toRadians(-1.04 / 3600); // X 축 회전 (radians)
	private double rY = Math.toRadians(-0.35 / 3600); // Y 축 회전 (radians)
	private double rZ = Math.toRadians(3.08 / 3600); // Z 축 회전 (radians)
	private double scale = -8.3 / 1e6; // 축척 변화 (scale factor)

	// 오차 보정값
	private double latitudeCorrection = -0.0018; // 위도 보정값 (미세한 차이 조정)
	private double longitudeCorrection = 0.0074; // 경도 보정값 (미세한 차이 조정)

	// 위도/경도를 직교 좌표로 변환
	public double[] latLonToCartesian(double lat, double lon, double h) {
		double radLat = Math.toRadians(lat);
		double radLon = Math.toRadians(lon);

		// 곡률 반경 N 계산
		double N = a / Math.sqrt(1 - e2 * Math.sin(radLat) * Math.sin(radLat));

		// 직교 좌표계 (X, Y, Z)로 변환
		double X = (N + h) * Math.cos(radLat) * Math.cos(radLon);
		double Y = (N + h) * Math.cos(radLat) * Math.sin(radLon);
		double Z = ((1 - e2) * N + h) * Math.sin(radLat);

		return new double[] { X, Y, Z };
	}

	// 헬머트 변환 적용 (Cartesian -> Cartesian)
	public double[] applyHelmert(double x, double y, double z) {
		double x_helmert = (1 + scale) * (x + rZ * y - rY * z) + tX;
		double y_helmert = (1 + scale) * (-rZ * x + y + rX * z) + tY;
		double z_helmert = (1 + scale) * (rY * x - rX * y + z) + tZ;

		return new double[] { x_helmert, y_helmert, z_helmert };
	}

	// 직교 좌표를 다시 위도/경도로 변환
	public double[] cartesianToLatLon(double x, double y, double z) {
		double lon = Math.atan2(y, x);
		double p = Math.sqrt(x * x + y * y);
		double lat = Math.atan2(z, p * (1 - e2)); // 근사값 시작

		double N;
		double h = 0;
		double lat_prev;
		do {
			lat_prev = lat;
			N = a / Math.sqrt(1 - e2 * Math.sin(lat_prev) * Math.sin(lat_prev));
			h = p / Math.cos(lat_prev) - N;
			lat = Math.atan2(z, p * (1 - e2 * (N / (N + h))));
		} while (Math.abs(lat - lat_prev) > 1e-12); // 반복하여 더 정확한 위도를 구함

		// 보정된 위도 및 경도 반환
		return new double[] { Math.toDegrees(lat) + latitudeCorrection, Math.toDegrees(lon) + longitudeCorrection, h };
	}

	public static double[] transform(double latitude, double longitude) {
		HelmertTransformation helmert = new HelmertTransformation();

		double height = 0; // 고도는 0으로 가정

		// 1. 위도/경도를 직교 좌표로 변환
		double[] cartesian = helmert.latLonToCartesian(latitude, longitude, height);
		System.out.println("직교 좌표계 (X, Y, Z): " + cartesian[0] + ", " + cartesian[1] + ", " + cartesian[2]);

		// 2. 헬머트 변환 적용
		double[] helmertResult = helmert.applyHelmert(cartesian[0], cartesian[1], cartesian[2]);
		System.out
				.println("헬머트 변환 후 (X, Y, Z): " + helmertResult[0] + ", " + helmertResult[1] + ", " + helmertResult[2]);

		// 3. 변환된 좌표를 다시 위도/경도로 변환
		double[] latLon = helmert.cartesianToLatLon(helmertResult[0], helmertResult[1], helmertResult[2]);
		System.out.println("헬머트 변환 후 위도/경도: " + latLon[0] + ", " + latLon[1]);

		return latLon;
	}

	// 변환 예시 함수
	public static void main(String[] args) {
		HelmertTransformation helmert = new HelmertTransformation();

		// 예시 좌표 (위도, 경도, 고도)
		double latitude = 36.321275660519596;
		double longitude = 127.39303838517404;
		double height = 0; // 고도는 0으로 가정

		// 1. 위도/경도를 직교 좌표로 변환
		double[] cartesian = helmert.latLonToCartesian(latitude, longitude, height);
		System.out.println("직교 좌표계 (X, Y, Z): " + cartesian[0] + ", " + cartesian[1] + ", " + cartesian[2]);

		// 2. 헬머트 변환 적용
		double[] helmertResult = helmert.applyHelmert(cartesian[0], cartesian[1], cartesian[2]);
		System.out
				.println("헬머트 변환 후 (X, Y, Z): " + helmertResult[0] + ", " + helmertResult[1] + ", " + helmertResult[2]);

		// 3. 변환된 좌표를 다시 위도/경도로 변환
		double[] latLon = helmert.cartesianToLatLon(helmertResult[0], helmertResult[1], helmertResult[2]);
		System.out.println("헬머트 변환 후 위도/경도: " + latLon[0] + ", " + latLon[1]);
	}
}
