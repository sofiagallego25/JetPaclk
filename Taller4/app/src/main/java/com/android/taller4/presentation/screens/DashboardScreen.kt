package com.android.taller4.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.taller4.domain.model.TipoTransaccion
import com.android.taller4.domain.model.Transaccion
import com.android.taller4.presentation.viewmodel.FinanzasViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: FinanzasViewModel = hiltViewModel()) {
    val transacciones by viewModel.transacciones.collectAsState()
    val totales by viewModel.totales.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App de Finanzas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ResumenCards(totales.balance, totales.ingresos, totales.gastos)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Transacciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(transacciones) { transaccion ->
                    TransaccionItem(
                        transaccion = transaccion,
                        onDelete = { viewModel.eliminarTransaccion(transaccion) }
                    )
                }
            }
        }

        if (showDialog) {
            AgregarTransaccionDialog(
                onDismiss = { showDialog = false },
                onConfirm = { desc, monto, tipo, cat ->
                    viewModel.agregarTransaccion(desc, monto, tipo, cat)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun ResumenCards(balance: Double, ingresos: Double, gastos: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Balance Total", style = MaterialTheme.typography.labelLarge)
            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = if (balance >= 0) Color(0xFF2E7D32) else Color.Red
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        InfoCard(
            modifier = Modifier.weight(1f),
            titulo = "Ingresos",
            monto = ingresos,
            color = Color(0xFF2E7D32),
            icon = Icons.Default.ArrowUpward
        )
        InfoCard(
            modifier = Modifier.weight(1f),
            titulo = "Gastos",
            monto = gastos,
            color = Color.Red,
            icon = Icons.Default.ArrowDownward
        )
    }
}

@Composable
fun InfoCard(modifier: Modifier, titulo: String, monto: Double, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(titulo, style = MaterialTheme.typography.labelMedium)
            }
            Text(
                text = formatCurrency(monto),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun TransaccionItem(transaccion: Transaccion, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(transaccion.descripcion, style = MaterialTheme.typography.bodyLarge)
                Text(transaccion.categoria, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                text = "${if (transaccion.tipo == TipoTransaccion.INGRESO) "+" else "-"} ${formatCurrency(transaccion.monto)}",
                color = if (transaccion.tipo == TipoTransaccion.INGRESO) Color(0xFF2E7D32) else Color.Red,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun AgregarTransaccionDialog(onDismiss: () -> Unit, onConfirm: (String, Double, TipoTransaccion, String) -> Unit) {
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoTransaccion.INGRESO) }
    var categoria by remember { mutableStateOf("Varios") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Transacción") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(
                    value = monto, 
                    onValueChange = { monto = it }, 
                    label = { Text("Monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = tipo == TipoTransaccion.INGRESO, onClick = { tipo = TipoTransaccion.INGRESO })
                    Text("Ingreso")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = tipo == TipoTransaccion.GASTO, onClick = { tipo = TipoTransaccion.GASTO })
                    Text("Gasto")
                }
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val valMonto = monto.toDoubleOrNull() ?: 0.0
                if (descripcion.isNotBlank() && valMonto > 0) {
                    onConfirm(descripcion, valMonto, tipo, categoria)
                }
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}