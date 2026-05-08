export type IntakeStatus = 'PENDING' | 'TAKEN' | 'SKIPPED';

export const INTAKE_STATUS_OPTIONS: Array<{ value: IntakeStatus; label: string }> = [
  { value: 'PENDING', label: 'Pendente' },
  { value: 'TAKEN', label: 'Tomado' },
  { value: 'SKIPPED', label: 'Pulado' }
];

export const INTAKE_STATUS_LABELS: Record<IntakeStatus, string> = {
  PENDING: 'Pendente',
  TAKEN: 'Tomado',
  SKIPPED: 'Pulado'
};
