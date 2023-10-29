import React from 'react';
import axios from 'axios';
import {useNotify} from 'react-admin';

const RefreshCharacters = () => {
    const notify = useNotify();
    const handleButtonClick = () => {
        const backendUrl = '/api/characters/refresh';

        axios.post(backendUrl)
            .then((response) => {
                notify('Characters data refreshed', 'info');
            })
            .catch((error) => {
                notify('Error when try to refresh characters data', 'error');
            });
    };

    return (
        <div>
            <button onClick={handleButtonClick}>Refresh Characters data</button>
        </div>
    );
};

export default RefreshCharacters;
