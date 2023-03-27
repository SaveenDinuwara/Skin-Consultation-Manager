import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WestminsterSkinConsultationManagerTest {

    @Test
    void nameShouldNotContainSpaces() {
        WestminsterSkinConsultationManager westconsultmgr = new WestminsterSkinConsultationManager();

        assertEquals(false,westconsultmgr.nameValidation("Saveen Liyanage") );
    }

    @Test
    void nameShouldNotContainNumbers(){
        WestminsterSkinConsultationManager westconsultmgr = new WestminsterSkinConsultationManager();

        assertEquals(false, westconsultmgr.nameValidation("Saveen1234"));
    }

    @Test
    void datesShouldBeSeparatedWithDash() {

        WestminsterSkinConsultationManager westconsultmgr = new WestminsterSkinConsultationManager();

        assertEquals(false, westconsultmgr.dobValidation("2021.12.21"));
    }

    @Test
    void mobileNumberShouldStartWithZeroAndSeven() {

        WestminsterSkinConsultationManager westconsultmgr = new WestminsterSkinConsultationManager();

        assertEquals(false, westconsultmgr.mobileValidation("0815675622"));
    }

}