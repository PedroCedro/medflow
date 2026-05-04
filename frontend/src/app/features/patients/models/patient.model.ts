import { RelationshipType } from './relationship-type.model';

export interface PatientRequest {
  name: string;
  birthDate: string | null;
  relationship: RelationshipType | null;
  notes: string | null;
}

export interface Patient {
  id: number;
  name: string;
  birthDate: string | null;
  relationship: RelationshipType | null;
  relationshipDisplayName: string | null;
  notes: string | null;
  active: boolean;
}
