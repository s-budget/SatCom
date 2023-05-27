import { ILink } from "./link";
import { ISatelite } from "./satelite";

export interface ITransmitter {
    transmId?: number;
    transmBaud?: number;
    transmFreq?: string;
    transmMode?: string;
    transmName?: string;
    links?: ILink;
    satId?: number;
    linkID?: number;
    owningSatellite?: string;
}

export const transmInit: ITransmitter = {
    transmName: ""
}