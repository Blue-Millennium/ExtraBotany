package io.grasspow.extrabotany.common.handler;

import static io.grasspow.extrabotany.api.ExtraBotanyAPI.logger;

public class MemeHandler {

    public static void spam() {
        if (!ConfigHandler.CLIENT.disableLogSpam.get()) {
            logger.info("=============================================================================================");
            logger.info("*  _______   ___  ___   ___________    _______         __                                   *");
            logger.info("* /\"     \"| |\"  \\/\"  | (\"     _   \")  /\"      \\       /\"\"\\                                  *");
            logger.info("*(: ______)  \\   \\  /   )__/  \\\\__/  |:        |     /    \\                                 *");
            logger.info("* \\/    |     \\\\  \\/       \\\\_ /     |_____/   )    /' /\\  \\                                *");
            logger.info("* // ___)_    /\\.  \\       |.  |      //      /    //  __'  \\                               *");
            logger.info("*(:      \"|  /  \\   \\      \\:  |     |:  __   \\   /   /  \\\\  \\                              *");
            logger.info("* \\_______) |___/\\___|      \\__|     |__|  \\___) (___/    \\___)                             *");
            logger.info("*           _______       ______     ___________        __       _____  ___    ___  ___      *");
            logger.info("*          |   _  \"\\     /    \" \\   (\"     _   \")      /\"\"\\     (\\\"   \\|\"  \\  |\"  \\/\"  | 	 *");
            logger.info("*          (. |_)  :)   // ____  \\   )__/  \\\\__/      /    \\    |.\\\\   \\    |  \\   \\  /  	 *");
            logger.info("*          |:     \\/   /  /    ) :)     \\\\_ /        /' /\\  \\   |: \\.   \\\\  |   \\\\  \\/   	 *");
            logger.info("*          (|  _  \\\\  (: (____/ //      |.  |       //  __'  \\  |.  \\    \\. |   /   /    	 *");
            logger.info("*          |: |_)  :)  \\        /       \\:  |      /   /  \\\\  \\ |    \\    \\ |  /   /     	 *");
            logger.info("*          (_______/    \\\"_____/         \\__|     (___/    \\___) \\___|\\____\\) |___/      	 *");
            logger.info("=============================================================================================");
            logger.info("*Thank you for installing ExtraBotany!                                                      *");
            logger.info("*Owner: ExtraMeteorP                                                                        *");
            logger.info("*Artist: MalayP, Gloridifice                                                                *");
            logger.info("*No more anime girl                                                                         *");
            logger.info("*If you want to disable it, check your config settings                                      *");
            logger.info("=============================================================================================");
            logger.info("*SPECIAL THANKS LIST                                                                        *");
            for (String key : ContributorListHandler.contributorsMap.keySet())
                logger.info("*" + String.format("%-24s %-65s", key + ":", ContributorListHandler.getRole(key)) + "*");
            logger.info("=============================================================================================");

        }
    }

}
