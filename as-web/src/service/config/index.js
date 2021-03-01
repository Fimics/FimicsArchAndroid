import {get, put} from '../index';
import * as api from '../../service/api';

export function getConfig(params) {
    return get(api.api.config)(params);
}

export function updateConfig(params) {
    return put(api.api.config)(params);
}
