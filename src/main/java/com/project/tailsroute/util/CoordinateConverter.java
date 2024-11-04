package com.project.tailsroute.util;

import org.locationtech.proj4j.*;

public class CoordinateConverter {

	public static double[] convertProj4j(double x, double y) {
		// CRSFactory 생성
		CRSFactory crsFactory = new CRSFactory();

		// 원본 및 목적지 좌표계 설정
		// EPSG:2097 보정안된 중부원점(bessel) // 홈페이지에 써있음. EPSG:2097라고.
		CoordinateReferenceSystem srcCrs = crsFactory.createFromName("EPSG:2097");
		CoordinateReferenceSystem destCrs = crsFactory.createFromName("EPSG:4326");

		// 좌표 변환을 위한 변환기 생성
		CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
		CoordinateTransform transform = ctFactory.createTransform(srcCrs, destCrs);

		// 변환할 좌표 설정
		ProjCoordinate srcCoord = new ProjCoordinate(x, y);
		ProjCoordinate destCoord = new ProjCoordinate();

		// 좌표 변환 수행
		transform.transform(srcCoord, destCoord);

		System.err.println("===========================================================");
		System.err.println("변환된 (Helmert 전) Latitude: " + destCoord.y);
        System.err.println("변환된 (Helmert 전) Longitude: " + destCoord.x);
		
        return HelmertTransformation.transform(destCoord.y, destCoord.x);
	}

}