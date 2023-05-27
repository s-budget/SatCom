import axios from 'axios';

export const getAllMessages = async() => {
    const res = await axios.get('/messages');
    return res.data;
}

export const createMessage = async(stationName: string, satelliteName: string, text: string, freq: number, mode: string, baud: number) => {
    const res = await axios.post("/messages", {stationName, satelliteName, text, freq, mode, baud });
    return res.data;
}

export const deleteAllMessages = async() => {
    const res = await axios.delete("/messages");
    return res.data;
}

export const deleteMessage =async (messageId: string) => {
    const res = await axios.delete("messages/" + messageId);
    return res.data;
    
}