package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.LineService;
import subway.ui.request.PathRequest;
import subway.ui.request.SectionRequest;

import static subway.integration.IntegrationFixture.*;

@DisplayName("지하철 경로 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private LineService lineService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineService.registerStation(LINE1.getId(), new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE5.getValue()));
        lineService.registerStation(LINE1.getId(), new SectionRequest(STATION_B.getName(), STATION_C.getName(), DISTANCE5.getValue()));
        lineService.registerStation(LINE2.getId(), new SectionRequest(STATION_B.getName(), STATION_D.getName(), DISTANCE10.getValue()));
        lineService.registerStation(LINE3.getId(), new SectionRequest(STATION_D.getName(), STATION_C.getName(), DISTANCE3.getValue()));
        lineService.registerStation(LINE3.getId(), new SectionRequest(STATION_C.getName(), STATION_E.getName(), DISTANCE3.getValue()));
    }

    @DisplayName("지하철 경로를 검색한다.")
    @Test
    void findPathTest() {
        final PathRequest request = new PathRequest(STATION_D.getName(), STATION_B.getName());

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/sections/path")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("경로를 찾을 수 없는 경우 bad request가 발생한다.")
    @Test
    void findUnablePathTest() {
        final PathRequest request = new PathRequest(STATION_D.getName(), STATION_F.getName());

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/sections/path")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}