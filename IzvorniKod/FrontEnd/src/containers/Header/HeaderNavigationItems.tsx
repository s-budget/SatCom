import { UserRole } from "../../models/userData";

export interface ISimpleHeaderItems {
    link: string;
    label: string;
}

export const getHeaderItems = (userRole: string): ISimpleHeaderItems[] => {
    if (userRole == UserRole.SatelliteAdmin) {
        return [
            {
                label: "SEND MESSAGE",
                link: "/send-message",
            },
            {
                label: "SATELLITES",
                link: "/satellites",
            },
            {
                label: "LINKS",
                link: "/links",
            },
            {
                label: "TRANSMITTERS",
                link: "/transmitters",
            },
        ];
    } else if (userRole == UserRole.SuperAdmin) {
        return [
            {
                label: "SEND MESSAGE",
                link: "/send-message",
            },
            {
                label: "USERS",
                link: "/users",
            },
        ];
    } else {
        return [
            {
                label: "SEND MESSAGE",
                link: "/send-message",
            },
        ];
    }
};
