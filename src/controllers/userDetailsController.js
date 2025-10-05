import userDetailsModel from '../models/userDetailsModel.js';

export const setUserDetails = async (req, res) => {
    const {body} = req;
    try {
        const user = await userDetailsModel.createOrUpdate(body);
        res.status(201).json({ message: 'User details saved successfully.', user });
    } catch (error) {
        res.status(500).json({ message: 'Error saving user details:', error: error.message });
    }
};

export const getUserDetails = async (req, res) => {
    try {
        const users = await userDetailsModel.find({ username: req.params.username });
        if (!users || users.length === 0) {
            return res.status(404).json({ message: 'No user details found for the given username.' });
        }
        res.status(200).json({ users });
    } catch (error) {
        res.status(500).json({ message: 'Error fetching user details:', error: error.message });
    }
};
