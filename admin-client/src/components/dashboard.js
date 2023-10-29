import * as React from 'react';
import {Grid} from '@mui/material';
import RefreshCharacters from "./refreshCharacters";

export const Dashboard = () => (
    <Grid container spacing={2} mt={1}>
        <Grid item xs={12} md={9}>
            <RefreshCharacters/>
        </Grid>
    </Grid>
);