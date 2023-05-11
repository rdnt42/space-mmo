import {Admin, Resource} from "react-admin";
import {dataProvider} from "./components/dataProvider";
import {EngineCreate, EngineEdit, EngineList} from "./components/engines";
import {CargoHookCreate, CargoHookEdit, CargoHookList} from "./components/cargoHooks";
import {FuelTankCreate, FuelTankEdit, FuelTankList} from "./components/fuelTanks";

const App = () => (
    <Admin dataProvider={dataProvider}>
        <Resource name="engines" list={EngineList} edit={EngineEdit} create={EngineCreate}/>
        <Resource name="cargoHooks" list={CargoHookList} edit={CargoHookEdit} create={CargoHookCreate}/>
        <Resource name="fuelTanks" list={FuelTankList} edit={FuelTankEdit} create={FuelTankCreate}/>
    </Admin>
);

export default App;