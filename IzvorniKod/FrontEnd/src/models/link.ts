export interface ILink {
    linkId?: number;
    linkBaud?: number;
    linkMode?: string;
    linkFreq?: number;
}

export const linkInit: ILink = {
    linkMode: "",
    linkFreq: 0
};
