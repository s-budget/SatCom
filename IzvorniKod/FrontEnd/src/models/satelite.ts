import { UserData } from "./userData";
import { ITransmitter } from "./transmitter";
import { getAllModes } from "../api/mode";

export interface ISatelite {
    satName: string;
    satId?: number;
    createdBy?: UserData;
    creationDate?: Date;
    transmitters?: ITransmitter[];
}

export const sateliteInit: ISatelite = {
    satName: "",
};


